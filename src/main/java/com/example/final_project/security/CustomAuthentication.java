package com.example.final_project.security;

import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@ToString
public class CustomAuthentication implements Authentication {
    private final Long id;
    private final String role;
    private final String username;
    private boolean authenticated;

    public CustomAuthentication(Long id, String role, String username) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.authenticated = true;
    }

    public CustomAuthentication(Long id, String role, String username, boolean authenticated) {
        this.id = id;
        this.role = role;
        this.username = username;
        this.authenticated = authenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role));
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return id;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return username;
    }
}
