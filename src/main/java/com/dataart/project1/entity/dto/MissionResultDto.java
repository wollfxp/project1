package com.dataart.project1.entity.dto;


import com.dataart.project1.entity.Mission;
import com.dataart.project1.entity.Squad;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MissionResultDto {
    private BigDecimal missionPay;
    private Integer missionCores;
    private Boolean isSuccessful;
    private Squad squad;
}
