package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import com.gmail.onishchenko.oleksii.agile.entity.UserInfo;
import com.gmail.onishchenko.oleksii.agile.exception.UserAlreadyExistsException;
import com.gmail.onishchenko.oleksii.agile.repository.UserInfoJpaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@link UserInfoServiceImpl} class is the implementation of
 * the {@link UserInfoService} interface provides methods to manage users.
 *
 * @see UserInfoService
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger log = LogManager.getLogger(UserInfoServiceImpl.class);

    /**
     * The encoder to crypt passwords
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * The repository that providing access to users
     */
    private final UserInfoJpaRepository userInfoJpaRepository;

    @Autowired
    public UserInfoServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserInfoJpaRepository userInfoJpaRepository) {
        log.info("Create instance of {}", UserInfoServiceImpl.class);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userInfoJpaRepository = userInfoJpaRepository;
    }

    /**
     * Add a new user
     *
     * @param userInfoDto a user data transfer object
     * @return a data transfer object that contains added information
     * @throws UserAlreadyExistsException if the user with the same login
     *                                    already exists in database
     */
    @Transactional
    @Override
    public UserInfoDto add(UserInfoDto userInfoDto) {
        log.debug("Adding user {}", userInfoDto.getLogin());
        userInfoJpaRepository.findByLogin(userInfoDto.getLogin())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException();
                });
        UserInfo userInfo = new UserInfo(userInfoDto.getLogin(), bCryptPasswordEncoder.encode(userInfoDto.getPassword()));
        UserInfo createdUser = userInfoJpaRepository.saveAndFlush(userInfo);
        log.trace("User was saved: login = {}", createdUser.getLogin());
        return new UserInfoDto(createdUser);
    }

    /**
     * Check if the user with specified login already exists
     *
     * @param login the user's login
     * @return <code>true</code> if and only if a user with
     * specified login exists; <code>false</code> otherwise.
     */
    @Transactional(readOnly = true)
    @Override
    public boolean existsByLogin(String login) {
        boolean present = userInfoJpaRepository.findByLogin(login).isPresent();
        log.debug("Login = {}, present = {}", login, present);
        return present;
    }
}
