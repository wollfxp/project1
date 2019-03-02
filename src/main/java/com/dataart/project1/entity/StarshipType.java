package com.dataart.project1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class StarshipType {

    @Id
    @GeneratedValue
    private Long id;

    private Float health;
    private Float damage; // dps

    @Enumerated(EnumType.STRING)
    private DamageType damageType;

    private String type;
    private String name;

    @ElementCollection
    @MapKeyEnumerated(EnumType.STRING)
    private Map<DamageType,Float> resists;

    @JsonIgnore
    private Boolean isEnemyOnly;

    private Integer creditCost;
    private Integer coreCost;

}
