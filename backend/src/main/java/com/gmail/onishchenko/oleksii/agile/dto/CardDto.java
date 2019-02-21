package com.gmail.onishchenko.oleksii.agile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.gmail.onishchenko.oleksii.agile.entity.Card;
import com.gmail.onishchenko.oleksii.agile.entity.Status;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static java.util.Objects.nonNull;

/**
 * The Card Data Transfer Object to encapsulate card information
 */
public class CardDto {
    private Long id;

    /**
     * The primary key
     */
    @NotEmpty
    private String text;

    /**
     * The task status
     */
    @NotNull
    private Status status;

    /**
     * The user login who created the task
     */
    @JsonIgnore
    private String userLogin;

    public CardDto() {
    }

    public CardDto(Card card) {
        id = card.getId();
        text = card.getText();
        status = card.getStatus();

        UserInfo userInfo = card.getUserInfo();
        if (nonNull(userInfo)) {
            userLogin = userInfo.getLogin();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Status getStatus() {
        return status;
    }

    @JsonSetter("status")
    public void setStatus(String status) {
        this.status = Status.fromString(status);
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardDto cardDto = (CardDto) o;
        return Objects.equals(id, cardDto.id) &&
                Objects.equals(text, cardDto.text) &&
                status == cardDto.status &&
                Objects.equals(userLogin, cardDto.userLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, status, userLogin);
    }

    @Override
    public String toString() {
        return "CardDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", userLogin='" + userLogin + '\'' +
                '}';
    }
}
