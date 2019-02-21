package com.gmail.onishchenko.oleksii.agile.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The rest controller to process request related to users information
 */
@RestController
@RequestMapping("/api")
public class SecurityController {

    private static final Logger log = LogManager.getLogger(SecurityController.class);

    @Autowired
    public SecurityController() {
        log.info("Create instance of {}", SecurityController.class);
    }

    /**
     * @return the login of the authenticated user
     */
    @GetMapping("/users/me")
    public String me() {
        return log.traceExit(SecurityContextHolder.getContext()
                .getAuthentication()
                .getName()
        );
    }
}
