package com.gmail.onishchenko.oleksii.agile.security;

import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LogManager.getLogger(JWTAuthenticationFilter.class);

    private final UserInfoService userInfoService;

    public JWTAuthenticationFilter(UserInfoService userInfoService) {
        log.info("Create instance of {}", JWTAuthenticationFilter.class);
        this.userInfoService = userInfoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Authentication...");
        Authentication authentication = TokenAuthenticationService
                .getAuthentication(httpServletRequest);

        if (nonNull(authentication) && userInfoService.existsByLogin(authentication.getName())) {
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
