package com.gmail.onishchenko.oleksii.agile.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

class CardTest {
    @Test
    void equals() {
        EqualsVerifier.forClass(Card.class)
                .usingGetClass()
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}