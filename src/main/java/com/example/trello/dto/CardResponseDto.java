package com.example.trello.dto;

import com.example.trello.entity.Card;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardResponseDto extends ApiResponseDto{
    private String cardName;
    private String cardDescription;
//    private String cardColor;
//    private Long columnId;

    public CardResponseDto(Card card) {
        this.cardName = card.getCardName();
        this.cardDescription = card.getCardDescription();
//        this.columnId = card.getColumns().getId();
    }


}