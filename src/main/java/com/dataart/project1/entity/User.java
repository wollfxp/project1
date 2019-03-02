package com.dataart.project1.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"ownedShips"})
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    private Boolean premium;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private SecurityUser securityUser;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Starship> ownedShips;

    private Integer hangarSize;
    private BigInteger aiCores;
    private BigDecimal credits;
}
