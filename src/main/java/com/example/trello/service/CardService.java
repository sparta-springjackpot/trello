package com.example.trello.service;

import com.example.trello.dto.CardRequestDto;
import com.example.trello.dto.CardResponseDto;
import com.example.trello.entity.Card;
import com.example.trello.entity.Columns;
import com.example.trello.repository.CardRepository;
import com.example.trello.repository.ColumnsRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
//    private final UserRepository userRepository;
    private final ColumnsRepository columnsRepository;
//    private final WorkerRepository workerRepository;

    // 카드 생성 API
    public CardResponseDto createCard(Long columnId, CardRequestDto cardRequestDto) {
        Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new NullPointerException("Could Not Found Column"));

        Card card = new Card(columns, cardRequestDto);
        cardRepository.save(card);

        return new CardResponseDto(card);
    }

    // 카드 수정 API
    public void updateCard(Long id, CardRequestDto cardRequestDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("수정할 수 있는 Card 를 찾을 수 없습니다."));
        card.update(cardRequestDto);

        cardRepository.save(card);
    }

    // 카드 삭제 API
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("삭제할 수 있는 Card 가 존재하지 않습니다."));
        cardRepository.delete(card);
    }
}
