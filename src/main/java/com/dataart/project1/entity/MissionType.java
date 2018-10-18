package com.dataart.project1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
public class MissionType {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer level;
    private Integer risk; // damage to ship
    private Integer reward;

    private Integer rewardCoresMin;
    private Integer rewardCoresMax;
    private Float rewardCoresChance;

    private Integer length; //fuel cost
    private Integer duration; //in seconds

    @OneToMany
    private List<MissionEnemySpawn> enemySpawns;

    private Integer maxFleetSize;
}
