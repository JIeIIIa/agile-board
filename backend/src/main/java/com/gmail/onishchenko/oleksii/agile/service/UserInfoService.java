package com.gmail.onishchenko.oleksii.agile.service;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;

public interface UserInfoService {
    UserInfoDto add(UserInfoDto userInfoDto);

    boolean existsByLogin(String login);
}
