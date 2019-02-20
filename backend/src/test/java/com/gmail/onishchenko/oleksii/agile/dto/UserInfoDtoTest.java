package com.gmail.onishchenko.oleksii.agile.dto;

import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserInfoDtoTest {

    @Test
    void equals() {
        EqualsVerifier.forClass(UserInfo.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    void constructorWithParameter() {
        //Given
        UserInfo userInfo = new UserInfo("token-man", "some-password");
        UserInfoDto expected = new UserInfoDto();
        expected.setLogin("token-man");

        //When
        UserInfoDto result = new UserInfoDto(userInfo);

        //Then
        assertThat(result).isEqualToComparingFieldByField(expected);
    }
}