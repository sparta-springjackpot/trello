package com.example.trello.service;

import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.dto.TaskRequestDto;
import com.example.trello.dto.TaskResponseDto;
import com.example.trello.entity.Card;
import com.example.trello.entity.Columns;
import com.example.trello.entity.Task;
import com.example.trello.repository.CardRepository;
import com.example.trello.repository.ColumnsRepository;
import com.example.trello.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ColumnsRepository columnsRepository;

    public ResponseEntity<RestApiResponseDto> getTask(Long columnId, Long cardId) {
        try {
            validateCardAndColumnExistence(columnId, cardId);

            Task task = taskRepository.findByCardId(cardId).orElseThrow(
                    () -> new IllegalArgumentException("해당 카드에 설정된 날짜가 없습니다."));

            TaskResponseDto responseDto = new TaskResponseDto(task);

            return this.resultResponse(HttpStatus.OK, "설정한 날짜 조회", responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> createCardDates(Long columnId, Long cardId, TaskRequestDto requestDto) {
        try {
            Pair<Columns, Card> pair = validateCardAndColumnExistence(columnId, cardId);

            Columns columns = pair.getFirst();
            Card card = pair.getSecond();

            Optional<Task> chackColumnTask = taskRepository.findByCardId(columnId);

            Optional<Task> chackCardTask = taskRepository.findByCardId(cardId);

            if (chackColumnTask.isPresent() && chackCardTask.isPresent()) {
                log.info("중복 테스트");
                throw new IllegalArgumentException("해당 컬럼안 카드에 이미 설정된 날짜가 있습니다.");
            }

            Task task = new Task(requestDto, card, columns);

            task.setStartDate(LocalDateTime.now());

            taskRepository.save(task);

            return this.resultResponse(HttpStatus.CREATED, "날짜 설정 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> updateCardDates(Long columnId, Long cardId, Long dateId, TaskRequestDto requestDto) {
        try {
            validateCardAndColumnExistence(columnId, cardId);

            Task task = taskRepository.findById(dateId).orElseThrow(
                    () -> new IllegalArgumentException("설정한 날짜가 존재하지 않습니다."));

            task.setTitle(requestDto.getTitle());
            task.setEndDate(requestDto.getEndDate());

            task = taskRepository.save(task);

            return this.resultResponse(HttpStatus.OK, "날짜 수정 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> deleteCardDates(Long columnId, Long cardId, Long dateId) {
        try {
            validateCardAndColumnExistence(columnId, cardId);

            Task task = taskRepository.findById(dateId).orElseThrow(
                    () -> new IllegalArgumentException("설정한 날짜가 존재하지 않습니다."));

            taskRepository.delete(task);

            return this.resultResponse(HttpStatus.OK, "날짜 초기화 완료", null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public void processExpiredTasks() {
        LocalDateTime currentDate = LocalDateTime.now();
        log.info("현재 시간 " + currentDate);
        List<Task> expiredTasks = taskRepository.findByExpiredFalseAndEndDateBefore(currentDate);

        for (Task task : expiredTasks) {
            task.setExpired(true);
            taskRepository.save(task);
        }
    }

    private Pair<Columns, Card> validateCardAndColumnExistence(Long columnId, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));

        Columns column = columnsRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("해당 컬럼이 존재하지 않습니다."));

        if (!card.getColumns().getId().equals(columnId) || !card.getColumns().getId().equals(column.getId())) {
            throw new IllegalArgumentException("해당 카드는 지정된 컬럼에 속해 있지 않습니다.");
        }

        return Pair.of(column, card);
    }

    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }
}
