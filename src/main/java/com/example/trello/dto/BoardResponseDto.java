package com.example.trello.dto;

import com.example.trello.entity.Board;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {
    private String board_name;
    private String back_color;
    private String introduce;

    public BoardResponseDto(Board board) {
        this.board_name = board.getBoard_name();
        this.back_color = board.getBack_color();
        this.introduce = board.getIntroduce();
    }

}
