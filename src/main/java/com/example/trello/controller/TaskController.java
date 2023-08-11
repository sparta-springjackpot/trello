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

    @GetMapping("/columns/{columnid}/cards/{cardid}/dates")
    public ResponseEntity<RestApiResponseDto> getTask(@PathVariable Long columnid, @PathVariable Long cardid){
        return taskService.getTask(columnid, cardid);
    }

    @Transactional
    @PostMapping("/columns/{columnid}/cards/{cardid}/dates")
    public ResponseEntity<RestApiResponseDto> createCardDates(@PathVariable Long columnid, @PathVariable Long cardid, @RequestBody TaskRequestDto requestDto){
        return taskService.createCardDates(columnid, cardid, requestDto);
    }

    @PutMapping("/columns/{columnid}/cards/{cardid}/dates/{dateid}")
    public ResponseEntity<RestApiResponseDto> updateCardDates(@PathVariable Long columnid, @PathVariable Long cardid, @PathVariable Long dateid, @RequestBody TaskRequestDto requestDto) {
        return taskService.updateCardDates(columnid, cardid, dateid, requestDto);
    }

    @DeleteMapping("/columns/{columnid}/cards/{cardid}/dates/{dateid}")
    public ResponseEntity<RestApiResponseDto> deleteCardDates(@PathVariable Long columnid, @PathVariable Long cardid, @PathVariable Long dateid) {
        return taskService.deleteCardDates(columnid, cardid, dateid);
    }
}
