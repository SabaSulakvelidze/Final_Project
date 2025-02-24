package com.example.final_project.security;

import com.example.final_project.facade.AuthFacade;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Qualifier("jwtBasedAuthenticationFilter")
@RequiredArgsConstructor
public class JwtBasedAuthenticationFilter extends OncePerRequestFilter {

    private final AuthFacade authFacade;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader("Authorization");
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder
                .getContext()
                .setAuthentication(authFacade.authenticate(authenticationHeader.replace("Bearer ", "")));
        filterChain.doFilter(request, response);
    }
}
