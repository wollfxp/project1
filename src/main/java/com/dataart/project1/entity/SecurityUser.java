package com.dataart.project1.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class SecurityUser implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    private String username;
    private String password;
    private Boolean isAdmin;

    @Transient
    private List<UserRole> authorities;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new ArrayList<>();
            authorities.add(UserRole.USER);
            authorities.add(UserRole.APIUSER);
            if (isAdmin != null && isAdmin){
                authorities.add(UserRole.ADMIN);
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // as intended
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
