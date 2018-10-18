package com.dataart.project1.service;

import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.repo.StarshipTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminService {

    private StarshipRepo shipRepo;
    private StarshipTypeRepo starshipTypeRepo;

    @Autowired
    public AdminService(StarshipRepo shipRepo, StarshipTypeRepo starshipTypeRepo) {
        this.shipRepo = shipRepo;
        this.starshipTypeRepo = starshipTypeRepo;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public boolean giveShipToUser(String shipType, User user) {
        if (StringUtils.isEmpty(shipType) || StringUtils.isEmpty(user) || shipRepo.countByOwner(user) >= user.getHangarSize()) {
            return false;
        }
        var newShip = new Starship();
        newShip.setOwner(user);
        newShip.setName(shipType.toUpperCase());
        var type = starshipTypeRepo.findByType(shipType);
        if (type == null) {
            return false;
        }
        newShip.setType(type);
        newShip.setHealth(type.getHealth());
        return shipRepo.save(newShip) != null;
    }
}
