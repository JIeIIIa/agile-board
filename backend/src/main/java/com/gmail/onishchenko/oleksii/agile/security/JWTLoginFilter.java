package com.gmail.onishchenko.oleksii.agile.security;

import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.gmail.onishchenko.oleksii.agile.security.TokenAuthenticationService.retrieveUserInfoDto;
import static java.util.Collections.emptyList;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger log = LogManager.getLogger(JWTLoginFilter.class);

    public JWTLoginFilter(String url, String httpMethod, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url, httpMethod));
        setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException, ServletException {
        log.debug("Try to login...");

        UserInfoDto credentials = retrieveUserInfoDto(httpServletRequest);
        log.info("User logged in: " + credentials.getLogin());
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getLogin(),
                        credentials.getPassword(),
                        emptyList()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        TokenAuthenticationService
                .addAuthentication(response, authResult.getName());
    }
}
