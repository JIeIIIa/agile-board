package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.security.TokenAuthenticationService;
import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {SecurityController.class})
class SecurityControllerTest {

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
}