package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.User;
import com.dataart.project1.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public abstract class UserAwareController {

    @Autowired
    protected UserRepo userRepo;

    protected User getCurrentUser() {

        // for tests @WithMockUser
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepo.findByName(springUser.getUsername());
        } else {
            String loggedInUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepo.findByName(loggedInUsername);
        }

    }


}
