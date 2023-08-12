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

    //날짜 가져오기
    public ResponseEntity<RestApiResponseDto> getTask(Long columnId, Long cardId) {
        try {
            //메서드 분리 -> 가져온 컬럼아아디와 카드아이디를 검증후 일치하면 실행
            validateCardAndColumnExistence(columnId, cardId);

            //해당 카드에 설정된 날짜가 있는지
            Task task = taskRepository.findByCardId(cardId).orElseThrow(
                    () -> new IllegalArgumentException("해당 카드에 설정된 날짜가 없습니다."));

            // 새로운 객체 생성 파라미터 값으로 task값을 넣어준다.
            TaskResponseDto responseDto = new TaskResponseDto(task);
            //성공하면 http 상태코드 200, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.OK, "설정한 날짜 조회", responseDto);
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    //날짜 생성
    public ResponseEntity<RestApiResponseDto> createCardDates(Long columnId, Long cardId, TaskRequestDto requestDto) {
        try {
            //java fx의 pair를 이용하여 reply 객체에 데이터를 넣으려 사용
            Pair<Columns, Card> pair = validateCardAndColumnExistence(columnId, cardId);

            // 첫번째 파라미터를 columns 객체 생성
            Columns columns = pair.getFirst();
            // 두번째 파라미터를 card 객체 생성
            Card card = pair.getSecond();

            //columns 중복을 위한 변수
            Optional<Task> chackColumnTask = taskRepository.findByCardId(columnId);

            //card 중복을 위한 변수
            Optional<Task> chackCardTask = taskRepository.findByCardId(cardId);

            // 컬럼안 카드에 이미 설정한 날짜가 있는경우
            if (chackColumnTask.isPresent() && chackCardTask.isPresent()) {
                log.info("중복 테스트");
                throw new IllegalArgumentException("해당 컬럼안 카드에 이미 설정된 날짜가 있습니다.");
            }

            // 데이터를 담은 새로운 객체생성
            Task task = new Task(requestDto, card, columns);

            // 시작날짜는 현재 시간을 자동으로 담아줌
            task.setStartDate(LocalDateTime.now());

            // DB 저장
            taskRepository.save(task);

            //성공하면 http 상태코드 201, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.CREATED, "날짜 설정 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    //날짜 수정
    public ResponseEntity<RestApiResponseDto> updateCardDates(Long columnId, Long cardId, Long dateId, TaskRequestDto requestDto) {
        try {
            //메서드 분리 -> 가져온 컬럼아아디와 카드아이디를 검증후 일치하면 실행
            validateCardAndColumnExistence(columnId, cardId);

            //해당 카드에 설정된 날짜가 있는지
            Task task = taskRepository.findById(dateId).orElseThrow(
                    () -> new IllegalArgumentException("설정한 날짜가 존재하지 않습니다."));

            // 받아온 제목과 마감날짜를 설정함
            task.setTitle(requestDto.getTitle());
            task.setEndDate(requestDto.getEndDate());

            // db에 저장
            task = taskRepository.save(task);

            //성공하면 http 상태코드 200, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.OK, "날짜 수정 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    //날짜 삭제
    public ResponseEntity<RestApiResponseDto> deleteCardDates(Long columnId, Long cardId, Long dateId) {
        try {
            //메서드 분리 -> 가져온 컬럼아아디와 카드아이디를 검증후 일치하면 실행
            validateCardAndColumnExistence(columnId, cardId);

            //해당 카드에 설정된 날짜가 있는지
            Task task = taskRepository.findById(dateId).orElseThrow(
                    () -> new IllegalArgumentException("설정한 날짜가 존재하지 않습니다."));

            // task로 받아온 DB를 삭제함
            taskRepository.delete(task);
          
            //성공하면 http 상태코드 200, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.OK, "날짜 초기화 완료", new TaskResponseDto(task));
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // 설정한 날짜가 기간이 지났을 경우 이 메서드가 작동하여 Expired 값을 true를 바꿔주어 변경이 불가능하게
    // 바꿔준다.
    public void processExpiredTasks() {
        LocalDateTime currentDate = LocalDateTime.now();
        log.info("현재 시간 " + currentDate);
        List<Task> expiredTasks = taskRepository.findByExpiredFalseAndEndDateBefore(currentDate);

        for (Task task : expiredTasks) {
            task.setExpired(true);
            taskRepository.save(task);
        }
    }

    // 매서드 재사용을 위해 분리
    private Pair<Columns, Card> validateCardAndColumnExistence(Long columnId, Long cardId) {
        // 카드아이디가 있는지
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드가 존재하지 않습니다."));
        // 컬럼아이디가 있는지
        Columns column = columnsRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("해당 컬럼이 존재하지 않습니다."));

        // 해당 카드는 지정된 컬럼에 속해 있는지
        if (!card.getColumns().getId().equals(columnId) || !card.getColumns().getId().equals(column.getId())) {
            throw new IllegalArgumentException("해당 카드는 지정된 컬럼에 속해 있지 않습니다.");
        }

        //java fx의 pair를 이용하여 reply 객체에 데이터를 넣으려 사용
        return Pair.of(column, card);
    }

    // ResponseEntity에 RestApiResponseDto를 넣고 상태코드와 메시지를 반환하기 위해 사용
    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }
}
