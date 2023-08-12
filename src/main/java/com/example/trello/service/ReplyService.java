package com.example.trello.service;

import com.example.trello.dto.ReplyRequestDto;
import com.example.trello.dto.ReplyResponseDto;
import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.entity.Card;
import com.example.trello.entity.Columns;
import com.example.trello.entity.Reply;
import com.example.trello.entity.User;
import com.example.trello.repository.CardRepository;
import com.example.trello.repository.ColumnsRepository;
import com.example.trello.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CardRepository cardRepository;
    private final ColumnsRepository columnsRepository;

    //댓글 가져오기
    public ResponseEntity<RestApiResponseDto> getComment(Long columnId, Long cardId) {
        //실행
        try {
            //메서드 분리 -> 가져온 컬럼아아디와 카드아이디를 검증후 일치하면 실행
            validateCardAndColumnExistence(columnId, cardId);

            // DB에 저장된 카드아이디와 일치하는 댓글을 모두 가져옴
            // select * from reply where card_id
            List<Reply> commentList = replyRepository.findAllByCardId(cardId);
            //가져온 commentList를 람다 stream을 이용해 병렬처리를 이용함 반복문을 사용하지않고
            // 코드작성이 깔끔하다.
            // stream 객체 생성
            List<ReplyResponseDto> replyResponseDtoList = commentList.stream()
                    // 값을 변환하여 인자로 받는다.
                    .map(ReplyResponseDto::new)
                    // 컬렉션을 리스트로 변환.
                    .toList();
            //성공하면 http 상태코드 200, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.OK, "댓글 조회", replyResponseDtoList);
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    //댓글 생성
    public ResponseEntity<RestApiResponseDto> createComment(Long columnId,
                                                            Long cardId, ReplyRequestDto requestDto, User user
                                                            ) {
        try {
            //java fx의 pair를 이용하여 reply 객체에 데이터를 넣으려 사용
        Pair<Columns, Card> pair = validateCardAndColumnExistence(columnId, cardId);

        // 첫번째 파라미터를 columns 객체 생성
        Columns columns = pair.getFirst();
        // 두번째 파라미터를 card 객체 생성
        Card card = pair.getSecond();

        // 받아온 파라미터와 dto를 reply 엔티티를 생성후 엔티티에 생성자에 파라미터를 반환
        Reply reply = new Reply(requestDto, columns, card, user);
        // DB 저장
        replyRepository.save(reply);
            //성공하면 http 상태코드 201, 메세지, responsedto 객체를 json으로 반환.
        return this.resultResponse(HttpStatus.CREATED, "댓글 작성 완료", new ReplyResponseDto(reply));
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    // 댓글 수정
    @Transactional
    public ResponseEntity<RestApiResponseDto> updateComment(Long columnId, Long cardId, Long replyId, ReplyRequestDto requestDto, User user) {
        try {
            //메서드 분리 -> 가져온 컬럼아아디와 카드아이디를 검증후 일치하면 실행
            validateCardAndColumnExistence(columnId, cardId);

            // 댓글이 있는지
            Reply reply = replyRepository.findById(replyId).orElseThrow(() ->
                    new IllegalArgumentException("해당 댓글이 없습니다."));

            // 댓글 작성자인지
            Long writerId = reply.getUser().getId();
            Long loginId = user.getId();
            if (!writerId.equals(loginId)) {
                throw new IllegalArgumentException("작성자만 삭제/수정 할 수 있습니다.");
            }

            // 댓글 수정
            reply.update(requestDto);
            //성공하면 http 상태코드 200, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.OK, "댓글 수정 완료", new ReplyResponseDto(reply));
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    //댓글 삭제
    public ResponseEntity<RestApiResponseDto> deleteComment(Long columnId, Long cardId, Long replyId, User user) {
        try {
            //메서드 분리 -> 가져온 컬럼아아디와 카드아이디를 검증후 일치하면 실행
            validateCardAndColumnExistence(columnId, cardId);

            // 댓글이 있는지
            Reply reply = replyRepository.findById(replyId).orElseThrow(() ->
                    new IllegalArgumentException("해당 댓글이 없습니다."));

            // 댓글 작성자인지
            Long writerId = reply.getUser().getId();
            Long loginId = user.getId();

            if (!writerId.equals(loginId)) {
                throw new IllegalArgumentException("작성자만 삭제/수정 할 수 있습니다.");
            }

            // 댓글 삭제
            replyRepository.delete(reply);
            //성공하면 http 상태코드 200, 메세지, responsedto 객체를 json으로 반환.
            return this.resultResponse(HttpStatus.OK, "댓글 삭제 완료", new ReplyResponseDto(reply));
        } catch (IllegalArgumentException e) {
            // 이벤트가 발생하면 http 상태코드 400, 실패원인을 반환
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
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
