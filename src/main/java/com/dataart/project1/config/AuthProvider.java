package com.dataart.project1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthProvider implements AuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger(AuthProvider.class);

    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthProvider(@Qualifier("spaceUserDetailsService") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        UserDetails details = userDetailsService.loadUserByUsername(username);

        if (details == null){
            throw new UsernameNotFoundException("Sorry, I don't know you...");
        }

        if (authentication.getCredentials() != null) {
            String password = authentication.getCredentials().toString();

            if (details.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(username, password, details.getAuthorities());
            }
        } else {
            throw new BadCredentialsException("NO CREDENTIALS PROVIDED!");
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return (aClass.equals(UsernamePasswordAuthenticationToken.class));
    }
}
