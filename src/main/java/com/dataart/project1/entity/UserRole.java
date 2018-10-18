package com.dataart.project1.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

    ADMIN,USER,APIUSER;

    @Override
    public String getAuthority() {
        return name();
    }
}
