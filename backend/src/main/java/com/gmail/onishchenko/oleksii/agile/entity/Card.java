package com.gmail.onishchenko.oleksii.agile.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class Card implements Serializable {

    private static final long serialVersionUID = 7135667559702196003L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private Status status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_task_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserInfo userInfo;

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

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id) &&
                Objects.equals(text, card.text) &&
                status == card.status &&
                Objects.equals(userInfo, card.userInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text, status, userInfo);
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", status=" + status +
                ", userInfo=" + userInfo +
                '}';
    }
}
