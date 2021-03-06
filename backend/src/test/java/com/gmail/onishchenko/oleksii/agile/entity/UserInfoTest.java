package com.gmail.onishchenko.oleksii.agile.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserInfoTest {
    @Test
    void equals() {
        EqualsVerifier.forClass(UserInfo.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void toStringTest() {
        //When
        String result = new UserInfo().toString();

        //Then
        assertThat(result).doesNotContain("password");
    }
}