package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import com.gmail.onishchenko.oleksii.agile.exception.UserAlreadyExistsException;
import com.gmail.onishchenko.oleksii.agile.repository.UserInfoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserInfoServiceImplTest {

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserInfoJpaRepository userInfoJpaRepository;

    private UserInfoServiceImpl instance;

    @BeforeEach
    void setUp() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userInfoJpaRepository = mock(UserInfoJpaRepository.class);
        instance = new UserInfoServiceImpl(bCryptPasswordEncoder, userInfoJpaRepository);
    }

    @Nested
    class Add {

        private UserInfoDto userInfoDto;

        @BeforeEach
        void setUp() {
            userInfoDto = new UserInfoDto();
            userInfoDto.setLogin("login");
            userInfoDto.setPassword("password");
        }

        @Test
        void success() {
            //Given
            when(userInfoJpaRepository.findByLogin(any(String.class))).thenReturn(Optional.empty());
            when(userInfoJpaRepository.saveAndFlush(any(UserInfo.class)))
                    .thenAnswer(invocationOnMock -> {
                        UserInfo argument = (UserInfo) invocationOnMock.getArguments()[0];
                        argument.setId(7L);

                        return argument;
                    });

            //When
            UserInfoDto result = instance.add(userInfoDto);

            //Then
            assertThat(result.getLogin()).isEqualTo("login");
            assertThat(result.getPassword()).isNull();
            assertThat(result.getPasswordConfirmation()).isNull();
        }

        @Test
        void userAlreadyExists() {
            //Given
            when(userInfoJpaRepository.findByLogin(any(String.class))).thenReturn(Optional.of(new UserInfo()));

            //When
            assertThatThrownBy(()->instance.add(userInfoDto))
            .isInstanceOf(UserAlreadyExistsException.class);
        }
    }

    @Nested
    class FindByLogin {
        private String login = "someLogin";

        @Test
        void exists() {
            //Given
            when(userInfoJpaRepository.findByLogin(login)).thenReturn(Optional.of(new UserInfo()));

            //When
            boolean result = instance.existsByLogin(login);

            //Then
            assertThat(result).isTrue();
        }

        @Test
        void notExists() {
            //Given
            when(userInfoJpaRepository.findByLogin(login)).thenReturn(Optional.empty());

            //When
            boolean result = instance.existsByLogin(login);

            //Then
            assertThat(result).isFalse();
        }
    }
}