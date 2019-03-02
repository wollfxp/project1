package com.dataart.project1.entity;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @ManyToOne
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private Squad squad;

    private Float health;

    public Starship(StarshipType type, User user) {
        this.health = type.getHealth();
        this.type = type;
        this.owner = user;
        this.name = type.getName();
    }

    public DamageType getDamageType() {
        return type.getDamageType();
    }
}
