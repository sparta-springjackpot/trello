package com.example.trello.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CardListResponseDto {
    private List<CardResponseDto> cardsList;

    public  CardListResponseDto(List<CardResponseDto> cardsList) {
        this.cardsList = cardsList;
    }
}
