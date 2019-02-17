package com.gmail.onishchenko.oleksii.agile.security;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gmail.onishchenko.oleksii.agile.security.TokenAuthenticationService.retrieveUserInfoDto;

public class RegistrationFilter extends OncePerRequestFilter {

    private static final Logger log = LogManager.getLogger(RegistrationFilter.class);

    private final AntPathRequestMatcher antPathRequestMatcher;

    private final UserInfoService userInfoService;

    public RegistrationFilter(String url, String httpMethod, UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
        antPathRequestMatcher = new AntPathRequestMatcher(url, httpMethod);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (antPathRequestMatcher.matches(httpServletRequest)) {
            log.debug("Try to register a user");
            UserInfoDto credentials = retrieveUserInfoDto(httpServletRequest);
            credentials = userInfoService.add(credentials);

            TokenAuthenticationService
                    .addAuthentication(httpServletResponse, credentials.getLogin());
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
