package com.example.trello.controller;

import com.example.trello.dto.*;
import com.example.trello.entity.Board;
import com.example.trello.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
private final BoardService boardService;

    @PostMapping("/")
    public ResponseEntity<BoardResponseDto> createCard(@RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto boardResponseDto = boardService.postBoard(boardRequestDto);
        return new ResponseEntity<>(
                boardResponseDto,
                HttpStatus.OK
        );
    }

    @GetMapping("/")
    public ResponseEntity<List<Board>> getBoard(){
        List<Board> getboard = boardService.getBoard();
        return ResponseEntity.ok(getboard);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> updateCard(@PathVariable Long id, @RequestBody BoardRequestDto boardRequestDto) {
        boardService.updateBoard(id, boardRequestDto);
        ApiResponseDto apiResponseDto = new ApiResponseDto("보드를 수정했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteCard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        ApiResponseDto apiResponseDto = new ApiResponseDto("보드를 삭제했습니다.", HttpStatus.OK.value());
        return new ResponseEntity<>(
                apiResponseDto,
                HttpStatus.OK
        );
    }
}
