package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.security.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserRole userRole;

    public AuthUser(Long id, String email, String nickname, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new UserDetailsImpl(this).getAuthorities();
    }
}
