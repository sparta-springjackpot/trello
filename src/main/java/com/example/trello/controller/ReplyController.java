package com.example.trello.controller;

import com.example.trello.dto.ReplyRequestDto;
import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.security.UserDetailsImpl;
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

    @GetMapping("/columns/{columnid}/cards/{cardid}/replys")
    @ResponseBody
    public ResponseEntity<RestApiResponseDto> getComment(@PathVariable Long columnid,
                                                         @PathVariable Long cardid){
        return replyService.getComment(columnid ,cardid);
    }

    //댓글작성
    @PostMapping("/columns/{columnid}/cards/{cardid}/replys")
    public ResponseEntity<RestApiResponseDto> createComment(
            @PathVariable Long columnid,
            @PathVariable Long cardid,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ){
        this.tokenValidate(userDetails);

//        return replyService.createComment(cardId, requestDto, userDetails.getUser());
        return replyService.createComment(columnid, cardid, requestDto, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/columns/{columnid}/cards/{cardid}/replys/{replyid}")
    public ResponseEntity<RestApiResponseDto> updateComment(
            @PathVariable Long columnid,
            @PathVariable Long cardid,
            @PathVariable Long replyid,
            @RequestBody ReplyRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        this.tokenValidate(userDetails);
        return replyService.updateComment(columnid ,cardid, replyid, requestDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/columns/{columnid}/cards/{cardid}/replys/{replyid}")
    public ResponseEntity<RestApiResponseDto> deleteComment(
            @PathVariable Long columnid,
            @PathVariable Long cardid,
            @PathVariable Long replyid,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        this.tokenValidate(userDetails);
        return replyService.deleteComment(columnid ,cardid, replyid, userDetails.getUser());
    }

    public void tokenValidate(UserDetailsImpl userDetails) {
        try{
            userDetails.getUser();
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }
    }
}
