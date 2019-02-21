package com.gmail.onishchenko.oleksii.agile.repository;

import com.gmail.onishchenko.oleksii.agile.entity.Card;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The Card JPA repository is the interface providing access to cards
 */
public interface CardJpaRepository extends JpaRepository<Card, Long> {
    /**
     * Find all cards by the user
     *
     * @param userInfo the user
     * @return list of cards that specified user has created
     */
    List<Card> findAllByUserInfo(UserInfo userInfo);

    /**
     * Find the card by the owner and card id
     *
     * @param userInfo the user user
     * @param id       the card id
     * @return an {@code Optional} describing the card for
     * which the user and card id is equals to the specified parameter,
     * or an empty {@code Optional} if the card is not found
     */
    Optional<Card> findByUserInfoAndId(UserInfo userInfo, Long id);

    /**
     * Delete the card by the owner and card id
     *
     * @param userInfo the user user
     * @param id       the card id
     */
    void deleteByUserInfoAndId(UserInfo userInfo, Long id);
}
