package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {SecurityController.class})
class SecurityControllerTest {

    @MockBean
    private UserInfoService userInfoService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "qwerty-man")
    void me() throws Exception {
        //Given
        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/users/me");

        //When
        ResultActions perform = mockMvc.perform(get);

        //Then
        perform.andExpect(status().isOk())
                .andExpect(content().string("qwerty-man"));
    }
}