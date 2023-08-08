package com.example.trello.service;

import com.example.trello.dto.ReplyRequestDto;
import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.entity.Board_User;
import com.example.trello.entity.Card;
import com.example.trello.entity.Reply;
import com.example.trello.repository.CardRepository;
import com.example.trello.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CardRepository cardRepository;

    public ResponseEntity<RestApiResponseDto> createComment(
            Long cardId, ReplyRequestDto requestDto, Board_User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(
                () -> new IllegalArgumentException("해당 카드가 존재하지않습니다."));

        Reply reply = new Reply(requestDto, card, user);
    }
}
