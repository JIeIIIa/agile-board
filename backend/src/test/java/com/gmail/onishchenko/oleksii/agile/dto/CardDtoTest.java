package com.gmail.onishchenko.oleksii.agile.dto;

import com.gmail.onishchenko.oleksii.agile.entity.Card;
import com.gmail.onishchenko.oleksii.agile.entity.Status;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardDtoTest {

    @Test
    void equals() {
        EqualsVerifier.forClass(CardDto.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Nested
    class CopyConstructor {
        private Card card;
        private CardDto expected;

        @BeforeEach
        void setUp() {
            card = new Card();
            card.setId(777L);
            card.setText("Some text");
            card.setStatus(Status.DONE);

            expected = new CardDto();
            expected.setId(777L);
            expected.setText("Some text");
            expected.setStatus("Done");
        }

        @Test
        void userIsNull() {
            //When
            CardDto result = new CardDto(card);

            //Then
            assertThat(result).isEqualToComparingFieldByField(expected);
        }


        @Test
        void userIsNonNull() {
            //Given
            UserInfo userInfo = new UserInfo("token-man", "password");
            card.setUserInfo(userInfo);
            expected.setUserLogin("token-man");

            //When
            CardDto result = new CardDto(card);

            //Then
            assertThat(result).isEqualToComparingFieldByField(expected);
        }
    }
}