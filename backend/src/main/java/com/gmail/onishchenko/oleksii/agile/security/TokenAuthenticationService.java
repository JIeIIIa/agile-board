package com.gmail.onishchenko.oleksii.agile.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.onishchenko.oleksii.agile.dto.UserInfoDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class TokenAuthenticationService {
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String SECRET = "ThisIsASecret";
    static final String TOKEN_PREFIX = "Bearer";
    static final String HEADER_STRING = "Authorization";

    static void addAuthentication(HttpServletResponse response, String login) throws IOException {
        String JWT = createToken(login);
        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
        response.getWriter().write("{\"token\":\"" + JWT + "\"}");
    }

    public static String createToken(String login) {
        return Jwts.builder()
                    .setSubject(login)
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SECRET)
                    .compact();
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String login = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            return login != null ?
                    new UsernamePasswordAuthenticationToken(login, null, emptyList()) :
                    null;
        }
        return null;
    }

    static UserInfoDto retrieveUserInfoDto(HttpServletRequest request) throws IOException {
        String parameters = request.getParameterMap()
                .entrySet()
                .stream()
                .map(e -> "\"" + e.getKey() + "\":\"" + String.join(",", e.getValue()) + "\"")
                .collect(Collectors.joining(",", "{", "}"));
        return new ObjectMapper().readValue(parameters, UserInfoDto.class);
    }
}
