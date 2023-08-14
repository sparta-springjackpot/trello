package com.example.trello.entity;

import com.example.trello.dto.BoardRequestDto;
import com.example.trello.dto.CardRequestDto;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @OneToMany(mappedBy = "board",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board_User> board_userList = new ArrayList<>();

    @Column(nullable = false)
    private String board_name;

    @Column(nullable = false)
    private String back_color;

    @Column(nullable = false)
    private String introduce;

    public Board(BoardRequestDto boardRequestDto) {
        this.board_name = boardRequestDto.getBoard_name();
        this.back_color = boardRequestDto.getBack_color();
        this.introduce= boardRequestDto.getIntroduce();
    }


    public void update(BoardRequestDto boardRequestDto) {
        this.board_name = boardRequestDto.getBoard_name();
        this.back_color = boardRequestDto.getBack_color();
        this.introduce= boardRequestDto.getIntroduce();
    }

}
