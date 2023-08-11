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

    // 컬럼 생성 API
    @PostMapping
    public ResponseEntity<String> createColumn(@RequestBody ColumnRequestDto requestDto) {
        Long columnId = columnsService.createColumn(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Column creation successful");
    }

    // 컬럼 수정 API
    @PutMapping("/{columnId}")
    public ResponseEntity<ColumnResponseDto> editColumn(@PathVariable Long columnId,
                                                        @RequestBody ColumnRequestDto requestDto) {
        ColumnResponseDto responseDto = columnsService.editColumn(columnId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 컬럼 삭제 API
    @DeleteMapping("/{columnId}")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long columnId) {
        columnsService.deleteColumn(columnId);
        return ResponseEntity.noContent().build();
    }

    // 컬럼 순서 이동 API
    @PutMapping("/{columnId}/move/{newColumnNumber}")
    public ResponseEntity<String> moveColumnOrder(
            @PathVariable Long columnId,
            @PathVariable int newColumnNumber) {
        columnsService.moveColumnOrder(columnId, newColumnNumber);
        return ResponseEntity.ok("Move column order succeeded");
    }
}

