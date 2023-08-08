package com.example.trello.entity;

import com.example.trello.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Setter
@Getter
@NoArgsConstructor
@DynamicInsert
@Entity
@Table(name = "reply")
public class Reply extends TimeStamp{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Board_User user;

    public Reply(ReplyRequestDto requestDto, Card card, Board_User user) {
        this.id = id;
        this.content = requestDto.getContent();
        this.card = card;
        this.user = user;
    }

    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
