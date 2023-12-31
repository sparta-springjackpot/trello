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
public class Reply extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "column_id")
    private Columns columns;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Reply(ReplyRequestDto requestDto, Columns columns, Card card, User user) {
        this.id = id;
        this.content = requestDto.getContent();
        this.columns = columns;
        this.card = card;
        this.user = user;
    }

    public void update(ReplyRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
