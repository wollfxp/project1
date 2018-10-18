package com.dataart.project1.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Mission {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @ManyToOne
    private MissionType type;

    @ManyToOne
    private User user;

    private LocalDateTime creationTime;
    private LocalDateTime endTime;

    @OneToOne
    private Squad assignedSquad;
}
