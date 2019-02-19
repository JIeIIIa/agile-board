package com.gmail.onishchenko.oleksii.agile.repository;

import com.gmail.onishchenko.oleksii.agile.entity.Card;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardJpaRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUserInfo(UserInfo userInfo);

    Optional<Card> findByUserInfoAndId(UserInfo userInfo, Long id);

    void deleteByUserInfoAndId(UserInfo userInfo, Long id);
}
