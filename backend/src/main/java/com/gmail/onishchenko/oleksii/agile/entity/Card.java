package com.gmail.onishchenko.oleksii.agile.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * The entity class to encapsulate card information that
 * presents on the board
 */

@Entity
@Table
public class Card implements Serializable {

    private static final long serialVersionUID = 7135667559702196003L;

    /**
     * The primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The primary key
     */
    private String text;

    /**
     * The task status
     */
    private Status status;

    /**
     * The user who created the task
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_card_id")
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
        return Objects.equals(text, card.text) &&
                status == card.status &&
                Objects.equals(userInfo, card.userInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, status, userInfo);
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
