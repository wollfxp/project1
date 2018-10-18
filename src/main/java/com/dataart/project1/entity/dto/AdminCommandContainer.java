package com.dataart.project1.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class AdminCommandContainer {

    public AdminCommandContainer(AdminCommand command) {
        this.command = command;
    }

    private AdminCommand command;
    private Map<String,String> params;

}
