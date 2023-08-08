package com.example.trello.service;

import com.example.trello.dto.ColumnRequestDto;
import com.example.trello.dto.ColumnResponseDto;
import com.example.trello.entity.Columns;
import com.example.trello.repository.ColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColumnsService {

    private final ColumnsRepository columnsRepository;

    public Long createColumn(ColumnRequestDto requestDto) {
        Columns column = new Columns();
        column.setColumnName(requestDto.getColumnName());
        column.setColumnNumber(requestDto.getColumnNumber());
        return columnsRepository.save(column).getId();
    }

    public ColumnResponseDto editColumn(Long columnId, ColumnRequestDto requestDto) {
        Columns column = columnsRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found"));

        column.setColumnName(requestDto.getColumnName());
        column.setColumnNumber(requestDto.getColumnNumber());

        Columns savedColumn = columnsRepository.save(column);
        ColumnResponseDto responseDto = new ColumnResponseDto();
        responseDto.setId(savedColumn.getId());
        responseDto.setColumnName(savedColumn.getColumnName());
        responseDto.setColumnNumber(savedColumn.getColumnNumber());

        return responseDto;
    }

    public void deleteColumn(Long columnId) {
        columnsRepository.deleteById(columnId);
    }
}