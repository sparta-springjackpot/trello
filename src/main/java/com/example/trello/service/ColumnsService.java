package com.example.trello.service;

import com.example.trello.dto.ColumnRequestDto;
import com.example.trello.dto.ColumnResponseDto;
import com.example.trello.entity.Columns;
import com.example.trello.repository.ColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnsService {

    private final ColumnsRepository columnsRepository;

    // 컬럼 생성 로직
    public Long createColumn(ColumnRequestDto requestDto) {
        Columns column = new Columns();
        column.setColumnName(requestDto.getColumnName());
        column.setColumnNumber(requestDto.getColumnNumber());
        return columnsRepository.save(column).getId();
    }

    // 컬럼 수정 로직
    public ColumnResponseDto editColumn(Long columnId, ColumnRequestDto requestDto) {
        Columns column = columnsRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));

        column.setColumnName(requestDto.getColumnName());
        column.setColumnNumber(requestDto.getColumnNumber());

        Columns savedColumn = columnsRepository.save(column);
        ColumnResponseDto responseDto = new ColumnResponseDto();
        responseDto.setColumn_id(savedColumn.getId());
        responseDto.setColumnName(savedColumn.getColumnName());
        responseDto.setColumnNumber(savedColumn.getColumnNumber());

        return responseDto;
    }

    // 컬럼 삭제 로직
    public void deleteColumn(Long columnId) {
        columnsRepository.deleteById(columnId);
    }

    // 컬럼 순서 이동 로직
    public void moveColumnOrder(Long columnId, int newColumnNumber) {
        Columns column = columnsRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));

        int oldColumnNumber = column.getColumnNumber();

        if (oldColumnNumber != newColumnNumber) {
            List<Columns> allColumns = columnsRepository.findAll();

            for (Columns c : allColumns) {
                if (c.getColumnNumber().equals(newColumnNumber)) {
                    c.setColumnNumber(oldColumnNumber);
                    columnsRepository.save(c);
                    break;
                }
            }

            column.setColumnNumber(newColumnNumber);
            columnsRepository.save(column);
        }
    }
}
