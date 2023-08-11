package com.example.trello.controller;

import com.example.trello.dto.RestApiResponseDto;
import com.example.trello.dto.TaskRequestDto;
import com.example.trello.service.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/cards/{cardId}/dates")
    public ResponseEntity<RestApiResponseDto> getTask(@PathVariable Long cardId){
        return taskService.getTask(cardId);
    }

    @Transactional
    @PostMapping("/cards/{cardId}/dates")
    public ResponseEntity<RestApiResponseDto> createCardDates(@PathVariable Long cardId, @RequestBody TaskRequestDto requestDto){
        return taskService.createCardDates(cardId, requestDto);
    }

    @PutMapping("/cards/dates/{dateId}")
    public ResponseEntity<RestApiResponseDto> updateCardDates(@PathVariable Long dateId, @RequestBody TaskRequestDto requestDto) {
        return taskService.updateCardDates(dateId, requestDto);
    }

    @DeleteMapping("/cards/dates/{dateId}")
    public ResponseEntity<RestApiResponseDto> deleteCardDates(@PathVariable Long dateId) {
        return taskService.deleteCardDates(dateId);
    }
}
