package com.example.analisis.security;

import com.example.analisis.domain.entity.Authority;
import com.example.analisis.domain.entity.User;
import com.example.analisis.domain.enums.EState;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
    	boolean isEnabled = user.getState() != EState.INACTIVE.getId() && user.getState() != EState.ELIMINATED.getId();
        return new JwtUser(
                user.getId(),
                user.getFullName(),
                user.getUsername(),   
                user.getPassword(),
                user.getEmail(),
                user.getDateOfCreation(),
                user.getDateOfLastEntry(),
                user.getLastPasswordResetDate(),
                mapToGrantedAuthorities(user.getAuthorities()),
                isEnabled
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
