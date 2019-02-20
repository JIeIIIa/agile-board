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

@Service
public class CardServiceImpl implements CardService {

    private static final Logger log = LogManager.getLogger(CardServiceImpl.class);

    private final CardJpaRepository cardJpaRepository;

    private final UserInfoJpaRepository userInfoJpaRepository;

    @Autowired
    public CardServiceImpl(CardJpaRepository cardJpaRepository,
                           UserInfoJpaRepository userInfoJpaRepository) {
        log.info("Create instance of {}", CardServiceImpl.class);
        this.userInfoJpaRepository = userInfoJpaRepository;
        this.cardJpaRepository = cardJpaRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CardDto> retrieveAll(String login) {
        UserInfo userInfo = retrieveUserInfo(login);
        return cardJpaRepository.findAllByUserInfo(userInfo)
                .stream()
                .map(CardDto::new)
                .collect(toList());
    }

    @Transactional
    @Override
    public CardDto add(CardDto cardDto) {
        UserInfo userInfo = retrieveUserInfo(cardDto.getUserLogin());
        Card card = mapToCard(cardDto, userInfo);
        card = cardJpaRepository.saveAndFlush(card);

        return new CardDto(card);
    }

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

    @Transactional
    @Override
    public void delete(CardDto cardDto) {
        UserInfo userInfo = retrieveUserInfo(cardDto.getUserLogin());
        cardJpaRepository.deleteByUserInfoAndId(userInfo, cardDto.getId());
    }

    private UserInfo retrieveUserInfo(String userLogin) {
        return userInfoJpaRepository
                .findByLogin(userLogin)
                .orElseThrow(UserNotFoundException::new);
    }

    private Card mapToCard(CardDto cardDto, UserInfo userInfo) {
        Card card = new Card();
        card.setText(cardDto.getText());
        card.setStatus(cardDto.getStatus());
        card.setUserInfo(userInfo);

        return card;
    }
}
