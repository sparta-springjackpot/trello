package com.example.trello.controller;

import com.example.trello.dto.ColumnRequestDto;
import com.example.trello.dto.ColumnResponseDto;
import com.example.trello.service.ColumnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnsService columnsService;

    @PostMapping
    public ResponseEntity<String> createColumn(@RequestBody ColumnRequestDto requestDto) {
        Long columnId = columnsService.createColumn(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Column creation successful");
    }

    @PutMapping("/{columnId}")
    public ResponseEntity<ColumnResponseDto> editColumn(@PathVariable Long columnId,
                                                        @RequestBody ColumnRequestDto requestDto) {
        ColumnResponseDto responseDto = columnsService.editColumn(columnId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<String> deleteColumn(@PathVariable Long columnId) {
        columnsService.deleteColumn(columnId);
        return ResponseEntity.ok("Delete column succeeded");
    }
}
