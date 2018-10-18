package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.Mission;
import com.dataart.project1.entity.MissionType;
import com.dataart.project1.entity.User;
import com.dataart.project1.service.game.HangarService;
import com.dataart.project1.service.game.MissionService;
import com.dataart.project1.service.game.SquadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Controller
@PreAuthorize("isFullyAuthenticated()")
public class HomePageUi extends UserAwareController{

    private MissionService missionService;
    private static final String HOMEPAGE_VIEW = "homepage";

    @Autowired
    public HomePageUi(MissionService missionService) {
        this.missionService = missionService;
    }

    @RequestMapping("/")
    public String getHomeView(Model model) {
        User currentUser = getCurrentUser();
        Set<MissionType> availableMissions = missionService.getMissionTypes(currentUser);
        Set<Mission> missionsInProgress = missionService.fetchAndUpdateMissions(currentUser);
        model.addAttribute("missionTypes", availableMissions.stream().sorted(Comparator.comparing(MissionType::getId)).collect(toList()));
        model.addAttribute("userMissions", missionsInProgress);
        return HOMEPAGE_VIEW;
    }

}
