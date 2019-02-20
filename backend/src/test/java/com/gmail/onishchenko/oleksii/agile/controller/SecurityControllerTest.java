package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import com.gmail.onishchenko.oleksii.agile.exception.UserAlreadyExistsException;
import com.gmail.onishchenko.oleksii.agile.repository.UserInfoJpaRepository;
import com.gmail.onishchenko.oleksii.agile.security.TokenAuthenticationService;
import com.gmail.onishchenko.oleksii.agile.security.UserDetailsServiceImpl;
import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {SecurityController.class})
class SecurityControllerTest {

    private static UserInfoJpaRepository userInfoJpaRepository = mock(UserInfoJpaRepository.class);

    @Autowired
    private BCryptPasswordEncoder encoder;

    @TestConfiguration
    static class SecurityConfiguration {
        @Bean
        public UserDetailsServiceImpl userDetailsService() {
            return new UserDetailsServiceImpl(userInfoJpaRepository);
        }
    }

    @MockBean
    private UserInfoService userInfoService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        reset(userInfoService);
    }

    @DisplayName(value = "String me()")
    @Nested
    class Me {
        @Test
        void unauthorizedUser() throws Exception {
            //Given
            MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/users/me");

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isForbidden());
        }

        @Test
        void requestWithoutHeader() throws Exception {
            //Given
            MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/users/me");

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isForbidden());
        }


        @Test
        void requestWithHeaderButUserNotExists() throws Exception {
            //Given
            final String login = "qwerty-man";
            final String token = TokenAuthenticationService.createToken(login);
            when(userInfoService.existsByLogin(login)).thenReturn(false);
            MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/users/me")
                    .header("authorization", token);

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isForbidden());
        }

        @Test
        void success() throws Exception {
            //Given
            final String login = "qwerty-man";
            final String token = TokenAuthenticationService.createToken(login);
            when(userInfoService.existsByLogin(login)).thenReturn(true);
            MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/users/me")
                    .header("authorization", token);

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isOk())
                    .andExpect(content().string(login));
        }
    }

    @Nested
    class Registration {
        private MockHttpServletRequestBuilder post;

        private String login;

        @BeforeEach
        void setUp() {
            reset(userInfoService);
            login = "superSu";
            post = MockMvcRequestBuilders.post("/api/registration")
                    .param("login", login)
                    .param("password", "password");
        }

        @Test
        void wrongPasswordConfirmation() throws Exception {
            //Given
            post.param("passwordConfirmation", "anotherPassword");

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void userAlreadyExists() throws Exception {
            //Given
            post.param("passwordConfirmation", "anotherPassword");
            when(userInfoService.add(any(UserInfoDto.class))).thenThrow(UserAlreadyExistsException.class);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void success() throws Exception {
            //Given
            post.param("passwordConfirmation", "password");
            when(userInfoService.add(any(UserInfoDto.class)))
                    .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isCreated());
        }
    }
}