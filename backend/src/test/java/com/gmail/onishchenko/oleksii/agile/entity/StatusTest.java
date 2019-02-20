package com.gmail.onishchenko.oleksii.agile.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatusTest {

    private static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of("To do", Status.TO_DO),
                Arguments.of("To DO", Status.TO_DO),
                Arguments.of("In progress", Status.IN_PROGRESS),
                Arguments.of("iN PrOgreSs", Status.IN_PROGRESS),
                Arguments.of("Done", Status.DONE),
                Arguments.of("dOnE", Status.DONE)
        );
    }

    @ParameterizedTest
    @MethodSource(value = {"params"})
    void fromString(String status, Status expected) {
        //When
        Status result = Status.fromString(status);

        //Then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void unexpectedStatus() {
        //When
        assertThatThrownBy(() -> Status.fromString("some status"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}