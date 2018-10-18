package com.dataart.project1.service.security;

import com.dataart.project1.entity.User;
import com.dataart.project1.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SpaceUserDetailsService implements UserDetailsService {

    private UserDataService userService;

    @Autowired
    public SpaceUserDetailsService(UserDataService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User spaceUser = userService.findByName(s);
        if (spaceUser != null) {
            return spaceUser.getSecurityUser();
        }

        throw new UsernameNotFoundException("user \"" + s + "\" not found");
    }
}
