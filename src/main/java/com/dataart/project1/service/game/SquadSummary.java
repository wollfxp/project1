package com.dataart.project1.service.game;

import com.dataart.project1.entity.DamageType;
import com.dataart.project1.entity.Squad;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SquadSummary {
    private Map<DamageType, Double> dpsMap;
    private Squad squad;
}
