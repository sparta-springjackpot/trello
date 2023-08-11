package com.example.trello.service;

import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.dto.TaskRequestDto;
import com.example.trello.dto.TaskResponseDto;
import com.example.trello.entity.Card;
import com.example.trello.entity.Task;
import com.example.trello.repository.CardRepository;
import com.example.trello.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseEntity<RestApiResponseDto> getTask(Long cardId) {
        try {
            Task task = taskRepository.findByCardId(cardId).orElseThrow(
                    () -> new IllegalArgumentException("해당 카드에 설정된 날짜가 없습니다."));

            TaskResponseDto responseDto = new TaskResponseDto(task);

            return this.resultResponse(HttpStatus.OK, "설정한 날짜 조회", responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> createCardDates(Long cardId, TaskRequestDto requestDto) {
        try {
            Card card = cardRepository.findById(cardId).orElseThrow(
                    () -> new IllegalArgumentException("해당 카드가 존재하지않습니다."));

            Task task = new Task(requestDto, card);

            Optional<Task> chackTask = taskRepository.findByCardId(cardId);

            if (chackTask.isPresent()) {
                log.info("중복 테스트");
                throw new IllegalArgumentException("해당 카드에 이미 설정된 날짜가 있습니다.");
            }
            task.setStartDate(LocalDateTime.now());

            taskRepository.save(task);

            return this.resultResponse(HttpStatus.CREATED, " 날짜 설정 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> updateCardDates(Long dateId, TaskRequestDto requestDto) {
        try {
            Task task = taskRepository.findById(dateId).orElseThrow(
                    () -> new IllegalArgumentException("설정한 날짜가 존재하지 않습니다."));

            task.setTitle(requestDto.getTitle());
            task.setEndDate(requestDto.getEndDate());

            task = taskRepository.save(task);

            return this.resultResponse(HttpStatus.CREATED, " 날짜 수정 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> deleteCardDates(Long dateId) {
        try {
            Task task = taskRepository.findById(dateId).orElseThrow(
                    () -> new IllegalArgumentException("설정한 날짜가 존재하지 않습니다."));

            taskRepository.delete(task);

            return this.resultResponse(HttpStatus.CREATED, " 날짜 초기화 완료", null);
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

    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }
}
