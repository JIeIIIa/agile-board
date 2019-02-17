package com.gmail.onishchenko.oleksii.agile.controller;

import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class SecurityController {

    private static final Logger log = LogManager.getLogger(SecurityController.class);

    private final UserInfoService userInfoService;

    @Autowired
    public SecurityController(UserInfoService userInfoService) {
        log.info("Create instance of {}", SecurityController.class);
        this.userInfoService = userInfoService;
    }

    @GetMapping("/users/me")
    public String me() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
