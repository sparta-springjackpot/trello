package com.example.trello.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequestDto {
    private String cardName;
    private String cardDescription;
    private String cardColor;
    private Long cardNumber;
}
