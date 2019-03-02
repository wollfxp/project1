package com.dataart.project1.controller.api;

import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/hangar")
public class HangarApi {

    private StarshipRepo starshipRepo;
    private UserRepo userRepo;

    @Autowired
    public HangarApi(StarshipRepo starshipRepo, UserRepo userRepo) {
        this.starshipRepo = starshipRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/ships")
    @PreAuthorize("hasAuthority('APIUSER')")
    public List<Starship> getUserShips(Principal principal) {
        User user = userRepo.findByName(principal.getName());
        return starshipRepo.findAllByOwner(user);
    }

}
