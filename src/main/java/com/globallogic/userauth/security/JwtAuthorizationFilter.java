package com.globallogic.userauth.security;

import com.globallogic.userauth.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final String HEADER = "Authorization";

    private final String PREFIX = "Bearer ";

    private final JwtTokenManager jwtTokenManager;

    public JwtAuthorizationFilter(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!existsJwtToken(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        if (!jwtTokenManager.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        setAuthenticationContext(token, request);

        filterChain.doFilter(request, response);
    }

    private boolean existsJwtToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(HEADER);
        return !isEmpty(authenticationHeader) && authenticationHeader.startsWith(PREFIX);
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER);
        return header.split(" ")[1].trim();
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        User user = getUserDetails(token);

        //UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, null);
        //authenticationToken.setDetails(authenticationToken);

        //
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, null);
        //

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private User getUserDetails(String token) {
        String email = jwtTokenManager.getEmailFromToken(token);
        User user = new User();
        user.setEmail(email);

        return user;
    }

}
