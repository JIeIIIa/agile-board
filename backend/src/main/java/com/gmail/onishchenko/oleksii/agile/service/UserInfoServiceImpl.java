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

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger log = LogManager.getLogger(UserInfoServiceImpl.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserInfoJpaRepository userInfoJpaRepository;

    @Autowired
    public UserInfoServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserInfoJpaRepository userInfoJpaRepository) {
        log.info("Create instance of {}", UserInfoServiceImpl.class);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userInfoJpaRepository = userInfoJpaRepository;
    }

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

    @Transactional(readOnly = true)
    @Override
    public boolean existsByLogin(String login) {
        boolean present = userInfoJpaRepository.findByLogin(login).isPresent();
        log.debug("Login = {}, present = {}", login, present);
        return present;
    }
}