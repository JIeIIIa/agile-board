package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.CardDto;

import java.util.List;

public interface CardService {
    List<CardDto> retrieveAll(String login);

    CardDto add(CardDto cardDto);

    CardDto update(CardDto cardDto);

    void delete(CardDto cardDto);
}
