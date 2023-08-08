package com.example.trello.entity;

import com.example.trello.dto.CardRequestDto;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    private String cardName;

    @Column(nullable = false)
    private String cardDescription;

    @Column(nullable = false)
    private String cardColor;

    @Column(nullable = false)
    private Long cardNumber;

    public Card(CardRequestDto requestDto) {
        this.cardName = requestDto.getCardName();
        this.cardDescription = requestDto.getCardDescription();
        this.cardColor = requestDto.getCardColor();
        this.cardNumber = requestDto.getCardNumber();
    }
}