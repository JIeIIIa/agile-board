package com.gmail.onishchenko.oleksii.agile.configuration;

import com.gmail.onishchenko.oleksii.agile.security.JWTAuthenticationFilter;
import com.gmail.onishchenko.oleksii.agile.security.JWTLoginFilter;
import com.gmail.onishchenko.oleksii.agile.security.RegistrationFilter;
import com.gmail.onishchenko.oleksii.agile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger log = LogManager.getLogger(WebSecurityConfiguration.class);

    private final UserInfoService userInfoService;

    @Autowired
    public WebSecurityConfiguration(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
        log.info("Create instance of {}", WebSecurityConfiguration.class);
    }

    @Configuration
    public static class BCryptPasswordConfiguration {
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/manifest.json", "/service-worker.js", "/precache-manifest*").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login", "/api/registration").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/static/**/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // We filter the api/login requests
                .addFilterBefore(new JWTLoginFilter("/api/login", HttpMethod.POST.name(), authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new RegistrationFilter("/api/registration", HttpMethod.POST.name(), userInfoService),
                        UsernamePasswordAuthenticationFilter.class)
                // filter requests to check the presence of JWT in header
                .addFilterAfter(new JWTAuthenticationFilter(userInfoService),
                        UsernamePasswordAuthenticationFilter.class);
    }

}
