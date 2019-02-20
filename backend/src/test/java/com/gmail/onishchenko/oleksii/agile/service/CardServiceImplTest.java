package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.CardDto;
import com.gmail.onishchenko.oleksii.agile.entity.Card;
import com.gmail.onishchenko.oleksii.agile.entity.Status;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import com.gmail.onishchenko.oleksii.agile.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;
import com.gmail.onishchenko.oleksii.agile.repository.CardJpaRepository;
import com.gmail.onishchenko.oleksii.agile.repository.UserInfoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CardServiceImplTest {

    private CardJpaRepository cardJpaRepository;

    private UserInfoJpaRepository userInfoJpaRepository;

    private CardServiceImpl instance;

    private static final String LOGIN = "awesome-user";

    private UserInfo user;

    @BeforeEach
    void setUp() {
        cardJpaRepository = mock(CardJpaRepository.class);
        userInfoJpaRepository = mock(UserInfoJpaRepository.class);
        instance = new CardServiceImpl(cardJpaRepository, userInfoJpaRepository);
        user = new UserInfo(LOGIN, "top-secret");
        when(userInfoJpaRepository.findByLogin(LOGIN)).thenReturn(ofNullable(user));
    }

    @Nested
    class RetrieveAll {
        @Test
        void userNotFound() {
            //Given
            when(userInfoJpaRepository.findByLogin("user")).thenReturn(Optional.empty());

            //When
            assertThatThrownBy(() -> instance.retrieveAll("user"))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void success() {
            //Given
            Card first = new Card();
            first.setId(101L);
            first.setText("first");
            first.setStatus(Status.DONE);
            first.setUserInfo(user);
            Card second = new Card();
            second.setId(202L);
            second.setText("ыусщтв");
            second.setStatus(Status.IN_PROGRESS);
            second.setUserInfo(user);

            CardDto expectedFirst = new CardDto(first);
            CardDto expectedSecond = new CardDto(second);

            when(cardJpaRepository.findAllByUserInfo(user)).thenReturn(asList(first, second));

            //When
            List<CardDto> result = instance.retrieveAll(LOGIN);

            //Then
            assertThat(result).hasSize(2)
                    .containsExactly(expectedFirst, expectedSecond);
        }
    }

    @Nested
    class Add {
        @Test
        void userNotFound() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setText("text");
            cardDto.setStatus("Done");
            cardDto.setUserLogin("user");
            when(userInfoJpaRepository.findByLogin("user")).thenReturn(Optional.empty());

            //When
            assertThatThrownBy(() -> instance.add(cardDto))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void success() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setText("A simple text");
            cardDto.setStatus("Done");
            cardDto.setUserLogin(LOGIN);

            Card card = new Card();
            card.setText("A simple text");
            card.setStatus(Status.DONE);
            card.setUserInfo(user);

            CardDto expected = new CardDto(card);
            expected.setId(7L);

            when(cardJpaRepository.saveAndFlush(any(Card.class)))
                    .thenAnswer(invocationOnMock -> {
                        Card argument = (Card) invocationOnMock.getArguments()[0];
                        argument.setId(7L);

                        return argument;
                    });

            //When
            CardDto result = instance.add(cardDto);

            //Then
            assertThat(result).isEqualToComparingFieldByField(expected);
        }
    }

    @Nested
    class Update {
        @Test
        void userNotFound() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setText("text");
            cardDto.setStatus("Done");
            cardDto.setUserLogin("user");
            when(userInfoJpaRepository.findByLogin("user")).thenReturn(Optional.empty());

            //When
            assertThatThrownBy(() -> instance.update(cardDto))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void cardNotFound() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(7L);
            cardDto.setText("text");
            cardDto.setStatus("Done");
            cardDto.setUserLogin(LOGIN);

            when(cardJpaRepository.findByUserInfoAndId(user, 7L)).thenReturn(Optional.empty());

            //When
            assertThatThrownBy(() -> instance.update(cardDto))
                    .isInstanceOf(CardNotFoundException.class);
        }

        @Test
        void success() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(7L);
            cardDto.setText("New text");
            cardDto.setStatus("Done");
            cardDto.setUserLogin(LOGIN);

            Card card = new Card();
            card.setId(7L);
            card.setText("A simple text");
            card.setStatus(Status.TO_DO);
            card.setUserInfo(user);

            CardDto expected = new CardDto();
            expected.setId(7L);
            expected.setText("New text");
            expected.setStatus("Done");
            expected.setUserLogin(LOGIN);

            when(cardJpaRepository.findByUserInfoAndId(user, 7L))
                    .thenReturn(Optional.of(card));
            when(cardJpaRepository.saveAndFlush(any(Card.class)))
                    .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

            //When
            CardDto result = instance.update(cardDto);

            //Then
            assertThat(result).isEqualToComparingFieldByField(expected);
        }
    }

    @Nested
    class Delete {
        @Test
        void userNotFound() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(7L);
            cardDto.setUserLogin("user");
            when(userInfoJpaRepository.findByLogin("user")).thenReturn(Optional.empty());

            //When
            assertThatThrownBy(() -> instance.delete(cardDto))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void success() {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(7L);
            cardDto.setUserLogin(LOGIN);

            //When
            instance.delete(cardDto);

            //Then
            verify(cardJpaRepository, times(1)).deleteByUserInfoAndId(user, 7L);
        }
    }

}