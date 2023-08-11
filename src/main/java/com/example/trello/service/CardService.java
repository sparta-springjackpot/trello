package com.example.trello.service;

import com.example.trello.dto.CardRequestDto;
import com.example.trello.dto.CardResponseDto;
import com.example.trello.dto.WorkerRequestDto;
import com.example.trello.entity.Card;
import com.example.trello.entity.Columns;
import com.example.trello.entity.User;
import com.example.trello.entity.Worker;
import com.example.trello.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ColumnsRepository columnsRepository;
    private final WorkerRepository workerRepository;
    private final PermissionRepository permissionRepository;

    // 카드 생성
    public CardResponseDto createCard(Long columnId, CardRequestDto cardRequestDto) {
        Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new NullPointerException("Could Not Found Column"));

        Card card = new Card(columns, cardRequestDto);
        cardRepository.save(card);

        return new CardResponseDto(card);
    }

    // 카드 수정
    public void updateCard(Long id, CardRequestDto cardRequestDto) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("수정할 수 있는 Card 를 찾을 수 없습니다."));
        card.update(cardRequestDto);

        cardRepository.save(card);
    }

    // 카드 삭제
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("삭제할 수 있는 Card 가 존재하지 않습니다."));
        cardRepository.delete(card);
    }

    // 카드에 Worker 추가
    public void addWorker(Long cardId, WorkerRequestDto workerRequestDto) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NullPointerException("해당 카드가 존재하지 않습니다."));

        // 추가할 Worker 가 Board 에 초대되어 있는지 조회
        User newWorker = checkWorker(workerRequestDto);

        // 추가할 Wokrer 가 이미 추가되어있는지 조회  (중복조회)
        Optional<Worker> alreadyWorker = workerRepository.findAllByUserId(newWorker.getId());
        if(alreadyWorker.isPresent()) {
            throw new IllegalArgumentException("이미 작업자로 추가된 유저입니다.");
        }

        // 해당 User 를 Worker 추가
        Worker worker = new Worker(card, newWorker);
        workerRepository.save(worker);

    }

    // Worker 삭제
    public void deleteWorker(Long cardId, WorkerRequestDto workerRequestDto) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NullPointerException("해당 카드가 존재하지 않습니다."));

        // 삭제할 Worker, Board 초대되어 있는지 조회
        com.example.trello.entity.User newWorker = checkWorker(workerRequestDto);

        // 삭제할 Worker 아직 Worker 아닌지 조회
        Optional<Worker> alreadyWorker = workerRepository.findAllByUserId(newWorker.getId());
        if(alreadyWorker.isEmpty()) {
            throw new IllegalArgumentException("아직 작업자로 추가된 유저가 아닙니다.");
        }

        // 해당 Worker 삭제
        Worker worker = new Worker(card, newWorker);
        workerRepository.delete(worker);
    }

    private com.example.trello.entity.User checkWorker(WorkerRequestDto workerRequestDto) {
        com.example.trello.entity.User newWorker = userRepository.findByUsername(workerRequestDto.getWorker()).orElseThrow(() -> new NullPointerException("존재하지 않은 사용자 입니다."));
        permissionRepository.findById(newWorker.getId()).orElseThrow(() -> new NullPointerException("초대되지 않은 사용자입니다."));

        return newWorker;
    }

    // Card 를 다른 Column 으로 이동
    public void moveCard(Long id, Long columnId) {
        Columns columns = columnsRepository.findById(columnId).orElseThrow(() -> new NullPointerException("Could Not Found Column"));
        Card card = cardRepository.findById(id).orElseThrow(() -> new NullPointerException("Could Not Found Card"));
        card.moveCard(columns);
        cardRepository.save(card);
    }
}
