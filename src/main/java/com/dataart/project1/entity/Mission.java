package com.dataart.project1.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@ToString
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

    private ZonedDateTime creationTime;

    private ZonedDateTime endTime;

    @OneToOne
    private Squad assignedSquad;
}
