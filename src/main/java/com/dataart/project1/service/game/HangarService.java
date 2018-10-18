package com.dataart.project1.service.game;

import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.StarshipType;
import com.dataart.project1.entity.User;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.repo.StarshipTypeRepo;
import com.dataart.project1.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class HangarService {

    public static final Integer DEFAULT_HANGAR_SIZE = 10;
    private static final Integer REPAIR_PRICE = 12;
    private StarshipRepo shipRepo;
    private StarshipTypeRepo shiptypeRepo;
    private UserRepo userRepo;

    @Autowired
    public HangarService(StarshipRepo shipRepo, UserRepo userRepo, StarshipTypeRepo shiptypeRepo) {
        this.shipRepo = shipRepo;
        this.userRepo = userRepo;
        this.shiptypeRepo = shiptypeRepo;
    }

    @Transactional
    public boolean repairShip(Starship ship, User user) {

        if (ship.getSquad() != null && ship.getSquad().getActiveMission() != null) {
            return false;
        }

        StarshipType type = ship.getType();
        if (ship.getHealth() < type.getHealth()) {
            float missingHealth = type.getHealth() - ship.getHealth();
            BigDecimal repairsCost = BigDecimal.valueOf(missingHealth).multiply(new BigDecimal(REPAIR_PRICE));
            if (user.getCredits().compareTo(repairsCost) > -1) {
                ship.setHealth(type.getHealth());
                user.setCredits(user.getCredits().subtract(repairsCost));

                shipRepo.save(ship);
                userRepo.save(user);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public BigDecimal getRepairCostAll(List<Starship> ships) {
        BigDecimal repairCost = BigDecimal.ZERO;
        for (Starship s : ships) {
            if (s.getHealth() < s.getType().getHealth() && (s.getSquad() != null && s.getSquad().getActiveMission() == null)) {
                float missingHealth = s.getType().getHealth() - s.getHealth();
                repairCost = repairCost.add(BigDecimal.valueOf(missingHealth).multiply(new BigDecimal(REPAIR_PRICE)));
            }
        }
        return repairCost;
    }

    @Transactional
    public void buildShip(String type, User user) {

        StarshipType starshipType = shiptypeRepo.findByType(type);
        Long shipCount = shipRepo.countByOwner(user);

        if (starshipType != null && !starshipType.getIsEnemyOnly() && user.getHangarSize() > shipCount
                && user.getAiCores().compareTo(BigInteger.valueOf(starshipType.getCoreCost())) >= 0
                && user.getCredits().compareTo(BigDecimal.valueOf(starshipType.getCreditCost())) >= 0
        ) {
            Starship newShip = new Starship(starshipType, user);
            shipRepo.save(newShip);

            user.setCredits(user.getCredits().subtract(BigDecimal.valueOf(starshipType.getCreditCost())));
            user.setAiCores(user.getAiCores().subtract(BigInteger.valueOf(starshipType.getCoreCost())));
            userRepo.save(user);
        }

    }

    public Integer getUpgradeCreditCost(User user) {
        return ((user.getHangarSize() - DEFAULT_HANGAR_SIZE) / 10) * 10000;
    }

    public Integer getUpgradeCoreCost(User user) {
        return 5 + ((user.getHangarSize() - DEFAULT_HANGAR_SIZE) / 10) * 10;
    }

    @Transactional
    public void upgradeHangar(User user) {
        BigInteger coreCost = BigInteger.valueOf(getUpgradeCoreCost(user));
        BigDecimal creditCost = BigDecimal.valueOf(getUpgradeCreditCost(user));
        if (user.getAiCores().compareTo(coreCost) > -1
                && user.getCredits().compareTo(creditCost) > -1) {

            user.setAiCores(user.getAiCores().subtract(coreCost));
            user.setCredits(user.getCredits().subtract(creditCost));
            user.setHangarSize(user.getHangarSize() + 10);
            userRepo.save(user);
        }

    }

}
