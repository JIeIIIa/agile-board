package com.gmail.onishchenko.oleksii.agile.repository;

import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The Card JPA repository is the interface providing access to users information
 */
public interface UserInfoJpaRepository extends JpaRepository<UserInfo, Long> {
    /**
     * Find the user by the login
     *
     * @param login the user's login
     * @return an {@code Optional} describing the user for
     * which the login is equals to the specified parameter,
     * or an empty {@code Optional} if the user is not found
     */
    Optional<UserInfo> findByLogin(String login);
}
