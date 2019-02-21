package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.CardDto;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;

import java.util.List;

/**
 * The {@link CardService} interface provides methods to manage cards.
 *
 * @see CardServiceImpl
 */
public interface CardService {
    /**
     * Find all cards by the user
     *
     * @param login the user's login
     * @return list of cards that specified user has created
     * @throws UserNotFoundException
     * if the user with specified login not present
     */
    List<CardDto> retrieveAll(String login);

    /**
     * Add a new card
     *
     * @param cardDto a card data transfer object
     * @return a data transfer object that contains added information
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    CardDto add(CardDto cardDto);

    /**
     * Update card information by the user's (owner) login and card's id
     *
     * @param cardDto a card data transfer object
     * @return a data transfer object that contains updated information
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    CardDto update(CardDto cardDto);

    /**
     * Delete card with the specified id
     *
     * @param cardDto a card data transfer object that contains the
     *                user's login and card's id that should be deleted
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    void delete(CardDto cardDto);
}
