package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import com.gmail.onishchenko.oleksii.agile.exception.UserAlreadyExistsException;

/**
 * The {@link UserInfoService} interface provides methods to manage users.
 *
 * @see UserInfoServiceImpl
 */
public interface UserInfoService {
    /**
     * Add a new user
     *
     * @param userInfoDto a user data transfer object
     * @return a data transfer object that contains added information
     * @throws UserAlreadyExistsException if the user with the same login
     *                                    already exists in database
     */
    UserInfoDto add(UserInfoDto userInfoDto);

    /**
     * Check if the user with specified login already exists
     *
     * @param login the user's login
     * @return <code>true</code> if and only if a user with
     * specified login exists; <code>false</code> otherwise.
     */
    boolean existsByLogin(String login);
}
