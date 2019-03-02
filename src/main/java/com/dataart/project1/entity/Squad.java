package com.dataart.project1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Squad {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnore
    private User owner;

    @OneToMany
    @JsonBackReference
    private Set<Starship> ships;

    @OneToOne
    private Mission activeMission;

    @Transient
    private Boolean allShipsHaveEnoughFuel;

}
