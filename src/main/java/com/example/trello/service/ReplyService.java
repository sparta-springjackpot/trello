package com.example.trello.service;

import com.example.trello.dto.ReplyRequestDto;
import com.example.trello.dto.ReplyResponseDto;
import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.entity.Board_User;
import com.example.trello.entity.Card;
import com.example.trello.entity.Reply;
import com.example.trello.entity.User;
import com.example.trello.repository.CardRepository;
import com.example.trello.repository.ReplyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CardRepository cardRepository;

    public ResponseEntity<RestApiResponseDto> getComment(Long cardId) {
        List<Reply> commentList = replyRepository.findAllByCardId(cardId);
        List<ReplyResponseDto> replyResponseDtoList = commentList.stream()
                .map(ReplyResponseDto::new)
                .toList();
        return this.resultResponse(HttpStatus.OK,"댓글 조회",replyResponseDtoList);
    }

    public ResponseEntity<RestApiResponseDto> createComment(
            Long cardId, ReplyRequestDto requestDto, User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("해당 카드가 존재하지않습니다."));

        Reply reply = new Reply(requestDto, card, user);
        replyRepository.save(reply);
        return this.resultResponse(HttpStatus.CREATED, "댓글 작성 완료", new ReplyResponseDto(reply));
    }

    @Transactional
    public ResponseEntity<RestApiResponseDto> updateComment(Long cardId, ReplyRequestDto requestDto, User user) {
        // 댓글이 있는지
        Reply reply = replyRepository.findById(cardId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 없습니다."));

        // 댓글 작성자 혹은 관리자인지
        Long writerId = reply.getUser().getId();
        Long loginId = user.getId();
        if(!writerId.equals(loginId)){
            throw new IllegalArgumentException("작성자만 삭제/수정 할 수 있습니다.");
        }

        // 댓글 수정
        reply.update(requestDto);
        return this.resultResponse(HttpStatus.OK,"댓글 수정 완료",new ReplyResponseDto(reply));
    }

    public ResponseEntity<RestApiResponseDto> deleteComment(Long cardId, User user) {
        // 댓글이 있는지
        Reply reply = replyRepository.findById(cardId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 없습니다."));

        // 댓글 작성자 혹은 관리자인지
        Long writerId = reply.getUser().getId();
        Long loginId = user.getId();

        if(!writerId.equals(loginId)){
            throw new IllegalArgumentException("작성자만 삭제/수정 할 수 있습니다.");
        }

        // 댓글 삭제
        replyRepository.delete(reply);
        return this.resultResponse(HttpStatus.OK,"댓글 삭제 완료",null);
    }

    private ResponseEntity<RestApiResponseDto> resultResponse(HttpStatus status, String message, Object result) {
        RestApiResponseDto restApiResponseDto = new RestApiResponseDto(status.value(), message, result);
        return new ResponseEntity<>(
                restApiResponseDto,
                status
        );
    }
}
