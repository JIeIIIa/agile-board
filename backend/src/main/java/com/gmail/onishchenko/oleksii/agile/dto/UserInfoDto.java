package com.gmail.onishchenko.oleksii.agile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UserInfoDto {
    @NotNull
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordConfirmation;


    public UserInfoDto() {
    }

    public UserInfoDto(UserInfo userInfo) {
        this.login = userInfo.getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto that = (UserInfoDto) o;
        return Objects.equals(login, that.login) &&
                Objects.equals(password, that.password) &&
                Objects.equals(passwordConfirmation, that.passwordConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, passwordConfirmation);
    }

    @Override
    public String toString() {
        return "UserInfoDto{" +
                "login='" + login + '\'' +
                '}';
    }
}
