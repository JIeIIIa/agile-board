package com.gmail.onishchenko.oleksii.agile.security;

import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;
import com.gmail.onishchenko.oleksii.agile.repository.UserInfoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    private UserInfoJpaRepository userInfoJpaRepository;

    private UserDetailsServiceImpl instance;

    private static final String USER_LOGIN = "token-man";

    @BeforeEach
    void setUp() {
        userInfoJpaRepository = mock(UserInfoJpaRepository.class);
        instance = new UserDetailsServiceImpl(userInfoJpaRepository);
    }

    @Test
    void userNotExists() {
        //Given
        when(userInfoJpaRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.empty());

        //When
        assertThatThrownBy(() -> instance.loadUserByUsername(USER_LOGIN))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void success() {
        //Given
        UserInfo userInfo = new UserInfo(USER_LOGIN, "top-secret-password");
        userInfo.setId(777L);
        when(userInfoJpaRepository.findByLogin(USER_LOGIN)).thenReturn(Optional.of(userInfo));

        //When
        UserDetails result = instance.loadUserByUsername(USER_LOGIN);

        //Then
        assertThat(result.getUsername()).isEqualTo(USER_LOGIN);
        assertThat(result.getPassword()).isEqualTo("top-secret-password");
        Collection<? extends GrantedAuthority> authorities = result.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.contains(new SimpleGrantedAuthority("ROLE_USER"))).isTrue();
        assertThat(result.isEnabled()).isTrue();
    }
}