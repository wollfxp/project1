package com.dataart.project1.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class MarketListing {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Starship ship;
    private BigDecimal price;

    @OneToOne
    private User owner;
}
