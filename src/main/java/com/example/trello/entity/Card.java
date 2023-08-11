package com.example.trello.entity;

import com.example.trello.dto.CardRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    // 연관 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="column_id")
    private Columns columns;

//    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Worker> workers = new ArrayList<>();

    // title
    @Column(nullable = false)
    private String cardName;

    // description
    @Column(nullable = false)
    private String cardDescription;

    @Column
    private String cardColor;

    @Column(nullable = false)
    private Long cardNumber;

//    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Comment> commentList = new ArrayList<>();

    public Card(Columns coulmns, CardRequestDto requestDto) {
        this.cardName = requestDto.getCardName();
        this.cardDescription = requestDto.getCardDescription();
//        this.cardColor = requestDto.getCardColor();
        this.cardNumber = requestDto.getCardNumber();
    }


    public void update(CardRequestDto cardRequestDto) {
        this.cardName = cardRequestDto.getCardName();
        this.cardDescription = cardRequestDto.getCardDescription();
    }

//    public void moveCard(Columns columns) {
//        this.columns = columns;
//    }

}