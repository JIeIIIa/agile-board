package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.dto.CardDto;
import com.gmail.onishchenko.oleksii.agile.exception.CardNotFoundException;
import com.gmail.onishchenko.oleksii.agile.exception.UserNotFoundException;
import com.gmail.onishchenko.oleksii.agile.security.TokenAuthenticationService;
import com.gmail.onishchenko.oleksii.agile.service.CardService;
import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {CardController.class, ErrorController.class})
class CardControllerTest {
    @MockBean
    private CardService cardService;

    @MockBean
    private UserInfoService userInfoService;

    @Autowired
    private MockMvc mockMvc;

    private static final String LOGIN = "token-man";
    private static final String AUTH_HEADER = "Authorization";

    private String token;

    @BeforeEach
    void setUp() {
        reset(userInfoService, cardService);
        when(userInfoService.existsByLogin(LOGIN)).thenReturn(true);

        token = TokenAuthenticationService.createToken(LOGIN);
    }

    @Nested
    class RetrieveAllCards {
        private MockHttpServletRequestBuilder get;

        @BeforeEach
        void setUp() {
            get = MockMvcRequestBuilders.get("/api/cards");
        }

        @Test
        void success() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(7L);
            cardDto.setText("A simple task");
            cardDto.setStatus("To do");
            cardDto.setUserLogin(LOGIN);
            when(cardService.retrieveAll(LOGIN)).thenReturn(singletonList(cardDto));

            get.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(7)))
            .andExpect(jsonPath("$[0].text", is("A simple task")))
            .andExpect(jsonPath("$[0].status", is("To do")))
            .andExpect(jsonPath("$[0].login").doesNotExist());
        }

        @Test
        void failure() throws Exception {
            //Given
            when(cardService.retrieveAll(LOGIN)).thenThrow(UserNotFoundException.class);

            get.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void unauthorized() throws Exception {
            //When
            ResultActions perform = mockMvc.perform(get);

            //Then
            perform.andExpect(status().isForbidden());
        }
    }

    @Nested
    class AddCard {
        private MockHttpServletRequestBuilder post;

        @BeforeEach
        void setUp() {
            post = MockMvcRequestBuilders.post("/api/cards")
                    .param("text", "A simple text")
                    .param("status", "In progress");
        }

        @Test
        void success() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setText("A simple text");
            cardDto.setStatus("In progress");
            cardDto.setUserLogin(LOGIN);

            CardDto created = new CardDto();
            created.setId(777L);
            created.setText("A simple text");
            created.setStatus("In progress");
            created.setUserLogin(LOGIN);

            when(cardService.add(cardDto)).thenReturn(created);

            post.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(777)))
                    .andExpect(jsonPath("$.text", is("A simple text")))
                    .andExpect(jsonPath("$.status", is("In progress")))
                    .andExpect(jsonPath("$.login").doesNotExist());
        }

        @Test
        void throwsException() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setText("A simple text");
            cardDto.setStatus("In progress");
            cardDto.setUserLogin(LOGIN);

            when(cardService.add(cardDto)).thenThrow(UserNotFoundException.class);

            post.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void unauthorized() throws Exception {
            //When
            ResultActions perform = mockMvc.perform(post);

            //Then
            perform.andExpect(status().isForbidden());
        }
    }


    @Nested
    class UpdateCard {
        private MockHttpServletRequestBuilder put;

        private final Long ID = 777L;

        @BeforeEach
        void setUp() {
            put = MockMvcRequestBuilders.put("/api/cards/{id}", ID)
                    .param("text", "A simple text")
                    .param("status", "In progress");
        }

        @Test
        void success() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(ID);
            cardDto.setText("A simple text");
            cardDto.setStatus("In progress");
            cardDto.setUserLogin(LOGIN);

            CardDto updated = new CardDto();
            updated.setId(777L);
            updated.setText("A simple text");
            updated.setStatus("In progress");
            updated.setUserLogin(LOGIN);

            when(cardService.update(cardDto)).thenReturn(updated);

            put.header(AUTH_HEADER, token).param("id", "123");

            //When
            ResultActions perform = mockMvc.perform(put);

            //Then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(777)))
                    .andExpect(jsonPath("$.text", is("A simple text")))
                    .andExpect(jsonPath("$.status", is("In progress")))
                    .andExpect(jsonPath("$.login").doesNotExist());
        }

        @Test
        void userNotFound() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(ID);
            cardDto.setText("A simple text");
            cardDto.setStatus("In progress");
            cardDto.setUserLogin(LOGIN);

            when(cardService.update(cardDto)).thenThrow(UserNotFoundException.class);

            put.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(put);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void cardNotFound() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(ID);
            cardDto.setText("A simple text");
            cardDto.setStatus("In progress");
            cardDto.setUserLogin(LOGIN);

            when(cardService.update(cardDto)).thenThrow(CardNotFoundException.class);

            put.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(put);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void unauthorized() throws Exception {
            //When
            ResultActions perform = mockMvc.perform(put);

            //Then
            perform.andExpect(status().isForbidden());
        }
    }

    @Nested
    class DeleteCard {
        private MockHttpServletRequestBuilder delete;

        private final Long ID = 777L;

        @BeforeEach
        void setUp() {
            delete = MockMvcRequestBuilders.delete("/api/cards/{id}", ID);
        }

        @Test
        void success() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(ID);
            cardDto.setUserLogin(LOGIN);

            delete.header(AUTH_HEADER, token).param("id", "123");

            //When
            ResultActions perform = mockMvc.perform(delete);

            //Then
            perform.andExpect(status().isOk());
            verify(cardService, times(1)).delete(eq(cardDto));
            verifyNoMoreInteractions(cardService);
        }

        @Test
        void userNotFound() throws Exception {
            //Given
            CardDto cardDto = new CardDto();
            cardDto.setId(ID);
            cardDto.setUserLogin(LOGIN);

            doThrow(UserNotFoundException.class).when(cardService).delete(cardDto);

            delete.header(AUTH_HEADER, token);

            //When
            ResultActions perform = mockMvc.perform(delete);

            //Then
            perform.andExpect(status().isBadRequest());
        }

        @Test
        void unauthorized() throws Exception {
            //When
            ResultActions perform = mockMvc.perform(delete);

            //Then
            perform.andExpect(status().isForbidden());
        }
    }
}