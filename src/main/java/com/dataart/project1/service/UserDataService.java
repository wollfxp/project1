package com.dataart.project1.service;

import com.dataart.project1.entity.SecurityUser;
import com.dataart.project1.entity.User;
import com.dataart.project1.entity.dto.RegisterUserDto;
import com.dataart.project1.repo.SecurityUserRepo;
import com.dataart.project1.repo.UserRepo;
import com.dataart.project1.service.game.HangarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class UserDataService {

    private UserRepo userRepo;
    private SecurityUserRepo securityUserRepo;

    @Autowired
    public UserDataService(UserRepo userRepo, SecurityUserRepo securityUserRepo) {
        this.userRepo = userRepo;
        this.securityUserRepo = securityUserRepo;
    }

    public User save(User user) {
        return userRepo.save(user);
    }

    public User findByName(String name) {
        return userRepo.findByName(name);
    }

    @Transactional
    public User register(RegisterUserDto newUser) {

        if (newUser.getEmail() == null || newUser.getPassword() == null || newUser.getUsername() == null) {
            return null;
        }

        User createdUser = new User();
        createdUser.setName(newUser.getUsername());

        SecurityUser securityUser = new SecurityUser();
        securityUser.setUsername(newUser.getUsername());
        securityUser.setPassword(newUser.getPassword());

        securityUser.setUser(createdUser);
        createdUser.setSecurityUser(securityUser);

        createdUser.setHangarSize(HangarService.DEFAULT_HANGAR_SIZE);
        createdUser.setCredits(new BigDecimal(1000));
        createdUser.setAiCores(BigInteger.valueOf(10));

        userRepo.save(createdUser);

        return createdUser;
    }

    public SecurityUser tryLogin(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        return securityUserRepo.findByUsernameAndPassword(username, password);
    }

}
