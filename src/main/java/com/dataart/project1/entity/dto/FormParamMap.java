package com.dataart.project1.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FormParamMap {

    public FormParamMap() {
        params = new HashMap<>();
    }

    private Map<String, String> params;
}
