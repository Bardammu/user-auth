package com.globallogic.userauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpMethod.POST;

@Configuration
public class UserAuthSecurityConfig {

    private final JwtAuthorizationFilter jwtTokenFilter;


    public UserAuthSecurityConfig(@Autowired JwtAuthorizationFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests((authz) -> authz
                .antMatchers(POST, "/sign-up/**").permitAll()
                .anyRequest().authenticated());

        http
            .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> response.sendError(SC_UNAUTHORIZED, ex.getMessage()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
