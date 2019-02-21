package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.CardDto;
import com.gmail.onishchenko.oleksii.agile.entity.Card;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import com.gmail.onishchenko.oleksii.agile.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;
import com.gmail.onishchenko.oleksii.agile.repository.CardJpaRepository;
import com.gmail.onishchenko.oleksii.agile.repository.UserInfoJpaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * The {@link CardServiceImpl} class is the implementation of
 * the {@link CardService} interface provides methods to manage cards.
 *
 * @see CardService
 */
@Service
public class CardServiceImpl implements CardService {

    private static final Logger log = LogManager.getLogger(CardServiceImpl.class);

    /**
     * The repository that providing access to cards
     */
    private final CardJpaRepository cardJpaRepository;

    /**
     * The repository that providing access to users
     */
    private final UserInfoJpaRepository userInfoJpaRepository;

    @Autowired
    public CardServiceImpl(CardJpaRepository cardJpaRepository,
                           UserInfoJpaRepository userInfoJpaRepository) {
        log.info("Create instance of {}", CardServiceImpl.class);
        this.userInfoJpaRepository = userInfoJpaRepository;
        this.cardJpaRepository = cardJpaRepository;
    }

    /**
     * Find all cards by the user
     *
     * @param login the user's login
     * @return list of cards that specified user has created
     * @throws com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException if the user with specified login not present
     */
    @Transactional(readOnly = true)
    @Override
    public List<CardDto> retrieveAll(String login) {
        UserInfo userInfo = retrieveUserInfo(login);
        return cardJpaRepository.findAllByUserInfo(userInfo)
                .stream()
                .map(CardDto::new)
                .collect(toList());
    }

    /**
     * Add a new card
     *
     * @param cardDto a card data transfer object
     * @return a data transfer object that contains added information
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    @Transactional
    @Override
    public CardDto add(CardDto cardDto) {
        UserInfo userInfo = retrieveUserInfo(cardDto.getUserLogin());
        Card card = mapToCard(cardDto, userInfo);
        card = cardJpaRepository.saveAndFlush(card);

        return new CardDto(card);
    }

    /**
     * Update card information by the user's (owner) login and card's id
     *
     * @param cardDto a card data transfer object
     * @return a data transfer object that contains updated information
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    @Transactional
    @Override
    public CardDto update(CardDto cardDto) {
        UserInfo userInfo = retrieveUserInfo(cardDto.getUserLogin());
        Card card = cardJpaRepository.findByUserInfoAndId(userInfo, cardDto.getId())
                .orElseThrow(CardNotFoundException::new);
        card.setText(cardDto.getText());
        card.setStatus(cardDto.getStatus());
        cardJpaRepository.saveAndFlush(card);

        return cardDto;
    }

    /**
     * Delete card with the specified id
     *
     * @param cardDto a card data transfer object that contains the
     *                user's login and card's id that should be deleted
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    @Transactional
    @Override
    public void delete(CardDto cardDto) {
        UserInfo userInfo = retrieveUserInfo(cardDto.getUserLogin());
        cardJpaRepository.deleteByUserInfoAndId(userInfo, cardDto.getId());
    }

    /**
     * Retrieve information about a user with the specified login
     *
     * @param userLogin the user's login
     * @return the user information
     * @throws UserNotFoundException if the user with specified login
     *                               do not present in database
     */
    private UserInfo retrieveUserInfo(String userLogin) {
        return userInfoJpaRepository
                .findByLogin(userLogin)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Map a card data transfer object to a card
     */
    private Card mapToCard(CardDto cardDto, UserInfo userInfo) {
        Card card = new Card();
        card.setText(cardDto.getText());
        card.setStatus(cardDto.getStatus());
        card.setUserInfo(userInfo);

        return card;
    }
}
