package com.gmail.onishchenko.oleksii.agile.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum to encapsulate the task status
 */

public enum Status {
    TO_DO("To do"), IN_PROGRESS("In progress"), DONE("Done");

    /**
     * The value to represent to user
     */
    @JsonValue
    private String value;

    Status(String value) {
        this.value = value;
    }

    /**
     * Map a string value of a status to the enum value
     *
     * @param value the string status representation
     * @return the enum value of a task status
     * @throws IllegalArgumentException if status do not present
     *                                  in the list of enums
     */
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
