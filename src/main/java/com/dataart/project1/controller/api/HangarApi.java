package com.dataart.project1.controller.api;

import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.StarshipRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hangar")
public class HangarApi {

    private StarshipRepo starshipRepo;

    @Autowired
    public HangarApi(StarshipRepo starshipRepo) {
        this.starshipRepo = starshipRepo;
    }

    @GetMapping("/ships")
    @PreAuthorize("hasAuthority('APIUSER')")
    public List<Starship> getUserShips(@RequestParam("user") User user) {
        return starshipRepo.findAllByOwner(user);
    }

}
