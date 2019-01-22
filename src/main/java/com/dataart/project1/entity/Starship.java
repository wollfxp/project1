package com.dataart.project1.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Starship {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    private StarshipType type;

    @ManyToOne
    private User owner;

    @ManyToOne
    private Squad squad;
    private Float health;

    public DamageType getDamageType(){
        return type.getDamageType();
    }

    public Starship(StarshipType type, User user){
        this.health = type.getHealth();
        this.type = type;
        this.owner = user;
        this.name = type.getName();
    }
}
