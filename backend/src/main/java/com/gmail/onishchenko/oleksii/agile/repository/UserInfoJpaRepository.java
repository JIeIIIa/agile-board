package com.gmail.onishchenko.oleksii.agile.repository;

import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoJpaRepository extends JpaRepository<UserInfo, Long> {
    Optional<UserInfo> findByLogin(String login);
}
