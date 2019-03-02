package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.MissionStatus;
import com.dataart.project1.entity.MissionType;
import com.dataart.project1.entity.User;
import com.dataart.project1.entity.dto.FormParamMap;
import com.dataart.project1.service.game.HangarService;
import com.dataart.project1.service.game.MissionService;
import com.dataart.project1.service.game.SquadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;

@Controller
@RequestMapping("/mission")
@PreAuthorize("isFullyAuthenticated()")
public class MissionUi extends UserAwareController {

    private MissionService missionService;
    private SquadService squadService;
    private HangarService hangarService;
    private static final String MISSION_START_VIEW = "startmission";
    private static final String MISSION_RESULT_VIEW = "missionresult";

    @Autowired
    public MissionUi(MissionService missionService, SquadService squadService, HangarService hangarService) {
        this.missionService = missionService;
        this.squadService = squadService;
        this.hangarService = hangarService;
    }


    @GetMapping("/start")
    public String getMissionStartView(Model model, @RequestParam("id") Long typeId) {
        MissionType missionType = missionService.getMissionTypeById(typeId);
        String missionRisk;
        switch (missionType.getRisk()) {
            case 1:
                missionRisk = "Low";
                break;
            case 2:
                missionRisk = "Medium";
                break;
            case 3:
                missionRisk = "High";
                break;
            case 4:
                missionRisk = "Critical";
                break;
            default:
                missionRisk = "Unknown";
                break;
        }
        missionType.getEnemySpawns().sort(Comparator.comparing(a -> a.getEnemyType().getId()));
        model.addAttribute("missionType", missionType);
        model.addAttribute("missionRiskText", missionRisk);
        return MISSION_START_VIEW;
    }


    @PostMapping("/confirm")
    public String postSquadSelectForMission(Model model, @ModelAttribute("params") FormParamMap params) {
        User currentUser = getCurrentUser();
        var selectedSquad = Long.valueOf(params.getParams().get("squadId"));
        var missionTypeId = Long.valueOf(params.getParams().get("missionType"));
        missionService.createMission(missionTypeId, selectedSquad, currentUser);
        return "redirect:/";
    }


    @GetMapping("/results")
    public String getMissionResults(Model model, @RequestParam("id") Long missionId) {
        var mission = missionService.getMissionById(missionId);
        if (mission == null || mission.getStatus() == MissionStatus.COMPLETED || mission.getStatus() == MissionStatus.FAILED ||
                !mission.getUser().getId().equals(getCurrentUser().getId()) || !mission.getEndTime().isBefore(ZonedDateTime.now())) {
            return "redirect:/";
        }

        if (mission.getStatus().equals(MissionStatus.IN_PROGRESS)) {
            missionService.simulateMission(mission);
            var result = missionService.completeMission(mission);
            model.addAttribute("result", result);
        }
        model.addAttribute("mission", mission);
        return MISSION_RESULT_VIEW;
    }

}
