package com.dataart.project1.entity;

import org.springframework.security.core.GrantedAuthority;

public enum UserAuthority implements GrantedAuthority {

    ADMIN,USER,APIUSER;

    @Override
    public String getAuthority() {
        return name();
    }
}
