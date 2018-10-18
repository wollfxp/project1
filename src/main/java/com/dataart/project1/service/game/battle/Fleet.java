package com.dataart.project1.service.game.battle;

import com.dataart.project1.entity.DamageType;
import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.StarshipType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class Fleet {
    private Map<DamageType, Double> dpsMap;
    private Set<Starship> ships;

    public Fleet(Set<Starship> ships) {
        this.ships = ships;
    }

    public void dealDamage(DamageType type, Float amount) {
        for (Starship s : ships) {
            float resist = (s.getType().getResists().get(type) != null) ? s.getType().getResists().get(type) : 0;
            float damage = (amount / ships.size()) * (1.f - resist);
            s.setHealth(s.getHealth() - damage);
        }
    }

    public void removeDestroyed() {
        Set<Starship> newSet = new HashSet<>();
        for (Starship s : ships) {
            if (s.getHealth() > 0) {
                newSet.add(s);
            }
        }
        ships = newSet;
    }

    public void updateDpsMap() {
        dpsMap = ships.stream().map(Starship::getType).collect(Collectors.groupingBy(StarshipType::getDamageType, Collectors.summingDouble(StarshipType::getDamage)));

//        ships.stream()
//        .map(Starship::getDamageType)
//        .forEach(type -> {
//            Double dpsOfThisType = ships.stream().filter(s -> s.getType().getDamageType().equals(type)).map(s -> (double) s.getType().getDamage()).reduce(0.0, Double::sum);
//        dpsMap.put(type, dpsOfThisType);
//        });
    }
}
