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

    public ResponseEntity<RestApiResponseDto> getComment(Long columnId, Long cardId) {
        try {
            validateCardAndColumnExistence(columnId, cardId);

            List<Reply> commentList = replyRepository.findAllByCardId(cardId);
            List<ReplyResponseDto> replyResponseDtoList = commentList.stream()
                    .map(ReplyResponseDto::new)
                    .toList();
            return this.resultResponse(HttpStatus.OK, "댓글 조회", replyResponseDtoList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> createComment(Long columnId,
                                                            Long cardId, ReplyRequestDto requestDto, User user
                                                            ) {
        try {
        Pair<Columns, Card> pair = validateCardAndColumnExistence(columnId, cardId);

        Columns columns = pair.getFirst();
        Card card = pair.getSecond();

        Reply reply = new Reply(requestDto, columns, card, user);
        replyRepository.save(reply);
        return this.resultResponse(HttpStatus.CREATED, "댓글 작성 완료", new ReplyResponseDto(reply));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<RestApiResponseDto> updateComment(Long columnId, Long cardId, Long replyId, ReplyRequestDto requestDto, User user) {
        try {
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
            return this.resultResponse(HttpStatus.OK, "댓글 수정 완료", new ReplyResponseDto(reply));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public ResponseEntity<RestApiResponseDto> deleteComment(Long columnId, Long cardId, Long replyId, User user) {
        try {
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
            return this.resultResponse(HttpStatus.OK, "댓글 삭제 완료", new ReplyResponseDto(reply));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RestApiResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
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
