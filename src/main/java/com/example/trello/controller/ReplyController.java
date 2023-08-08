package com.example.trello.controller;

import com.example.trello.dto.ReplyRequestDto;
import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.security.UserDetailslmpl;
import com.example.trello.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("/cards/{cardId}/replys")
    @ResponseBody
    public ResponseEntity<RestApiResponseDto> getComment(@PathVariable Long cardId){
        return replyService.getComment(cardId);
    }

    //댓글작성
    @PostMapping("/cards/{cardId}/replys")
    public ResponseEntity<RestApiResponseDto> createComment(
            @PathVariable Long cardId,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailslmpl userDetails
            ){
        this.tokenValidate(userDetails);

//        return replyService.createComment(cardId, requestDto, userDetails.getUser());
        return replyService.createComment(cardId, requestDto, userDetails.getUser());
    }

    //댓글 수정
    @PutMapping("/replys/{reply_id")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long reply_id,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailslmpl userDetails
            ) {
        this.tokenValidate(userDetails);
        return replyService.updateComment(reply_id, requestDto, userDetails.getUser());
    }

    @DeleteMapping("/replys/{reply_id")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long reply_id,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailslmpl userDetails
    ) {
        this.tokenValidate(userDetails);
        return replyService.deleteComment(reply_id, requestDto, userDetails.getUser());
    }

    public void tokenValidate(UserDetailslmpl userDetails) {
        try{
            userDetails.getUser();
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }
}
