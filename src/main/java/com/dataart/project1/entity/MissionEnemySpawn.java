package com.dataart.project1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MissionEnemySpawn {

    @Id
    @GeneratedValue
    private Long Id;

    @ManyToOne
    private StarshipType enemyType;
    private Float percentage;
}
