package com.example.trello.service;

import com.example.trello.dto.BoardRequestDto;
import com.example.trello.dto.BoardResponseDto;
import com.example.trello.dto.CardResponseDto;
import com.example.trello.entity.Board;
import com.example.trello.entity.Card;
import com.example.trello.entity.Columns;
import com.example.trello.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardResponseDto postBoard(BoardRequestDto boardRequestDto){

        Board board = new Board(boardRequestDto);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    public List<Board> getBoard(){
     List<Board> boards = boardRepository.findAll();
     return boards;
    }

    public void updateBoard(Long id, BoardRequestDto boardRequestDto){
        Board board = boardRepository.findById(id).orElseThrow(() -> new NullPointerException("보드가 존재하지 않습니다."));
        board.update(boardRequestDto);
        boardRepository.save(board);
    }

    public void deleteBoard(Long id){
        boardRepository.deleteById(id);
    }

}
