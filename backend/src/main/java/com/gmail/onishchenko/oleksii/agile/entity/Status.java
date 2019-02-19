package com.gmail.onishchenko.oleksii.agile.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    TO_DO("To do"), IN_PROGRESS("In progress"), DONE("Done");

    @JsonValue
    private String value;

    Status(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Status fromString(String value) {
        for (Status status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Illegal value for Status: " + value);
    }
}
