package com.dataart.project1.controller.api;

import com.dataart.project1.entity.User;
import com.dataart.project1.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserApi {

    private UserDataService userDataService;

    @Autowired
    public UserApi(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('APIUSER')")
    public User getCurrentUserData(Principal principal) {
        return userDataService.findByName(principal.getName());
    }
}
