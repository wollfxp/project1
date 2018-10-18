package com.dataart.project1.service.game.battle;

import com.dataart.project1.entity.DamageType;
import com.dataart.project1.entity.Mission;
import com.dataart.project1.entity.MissionStatus;
import com.dataart.project1.entity.Starship;
import com.dataart.project1.repo.StarshipTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BattleService {

    private StarshipTypeRepo starshipTypeRepo;

    @Autowired
    public BattleService(StarshipTypeRepo starshipTypeRepo) {
        this.starshipTypeRepo = starshipTypeRepo;
    }

    public void simulateBattle(Mission mission) {
        Fleet playerFleet = new Fleet(mission.getAssignedSquad().getShips());
        Fleet enemyFleet = generateEnemyFleet(mission);

        while (playerFleet.getShips().size() > 0 && enemyFleet.getShips().size() > 0){
            playerFleet.updateDpsMap();
            for (DamageType type : playerFleet.getDpsMap().keySet()){
                enemyFleet.dealDamage(type, playerFleet.getDpsMap().get(type).floatValue());
            }
            enemyFleet.removeDestroyed();

            enemyFleet.updateDpsMap();
            for (DamageType type : enemyFleet.getDpsMap().keySet()){
                playerFleet.dealDamage(type, enemyFleet.getDpsMap().get(type).floatValue());
            }
            playerFleet.removeDestroyed();
        }

        if (playerFleet.getShips().size() > 0){
            mission.setStatus(MissionStatus.COMPLETED);
        } else {
            mission.setStatus(MissionStatus.FAILED);
        }
    }


    private Fleet generateEnemyFleet(Mission mission) {
        var enemyTypes = mission.getType().getEnemySpawns();
        var maxFleetSize = mission.getType().getMaxFleetSize();
        Set<Starship> fleetShips = new HashSet<>();
        for (var type : enemyTypes) {
            var percentageContainer = mission.getType().getEnemySpawns().stream().filter(s -> s.getEnemyType().equals(type.getEnemyType()) && s.getEnemyType().getIsEnemyOnly()).findFirst();
            Float spawnPercentage = percentageContainer.isPresent() ? percentageContainer.get().getPercentage() : 0;
            int amount = Math.round(maxFleetSize * spawnPercentage);
            for (int i = 0; i < amount; i++) {
                Starship newShip = new Starship(type.getEnemyType(), null); // owner as null for enemy
                fleetShips.add(newShip);
            }
        }
        return new Fleet(fleetShips);
    }
}
