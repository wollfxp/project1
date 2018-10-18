package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.Squad;
import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.User;
import com.dataart.project1.entity.dto.FormParamMap;
import com.dataart.project1.repo.MissionRepo;
import com.dataart.project1.repo.MissionTypeRepo;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.service.game.SquadService;
import com.dataart.project1.service.game.battle.Fleet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/squad")
@PreAuthorize("isFullyAuthenticated()")
public class SquadUi extends UserAwareController {

    private SquadService squadService;
    private StarshipRepo starshipRepo;
    private MissionTypeRepo missionTypeRepo;
    private static final String SQUAD_VIEW = "squadedit";
    private static final String SQUAD_SELECT_VIEW = "squadselect";

    @Autowired
    public SquadUi(SquadService squadService, StarshipRepo starshipRepo, MissionTypeRepo missionTypeRepo) {
        this.squadService = squadService;
        this.starshipRepo = starshipRepo;
        this.missionTypeRepo  = missionTypeRepo;
    }

    @PostMapping("/create")
    public String createNewSquad(Model model) {
        var newSquad = new Squad();
        newSquad.setOwner(getCurrentUser());
        squadService.save(newSquad);
        newSquad.setName("New Squad");
        squadService.save(newSquad);

        return "redirect:/hangar";
    }

    @GetMapping("/edit")
    public String getSquadUiView(Model model, @RequestParam("id") Long squadId) {
        User currentUser = getCurrentUser();
        Squad squad = squadService.getSquadById(squadId, currentUser);

        if (squad == null) {
            model.addAttribute("error", "Specified squad not found :(");
            return SQUAD_VIEW;
        }

        model.addAttribute("squad", squad);
        List<Starship> starships = starshipRepo.findAllByOwnerAndSquadIsNull(currentUser);
        model.addAttribute("ships", starships);
        return SQUAD_VIEW;
    }

    @PostMapping("/edit")
    public String postSquadUiAction(Model model, @RequestParam("id") Long squadId,
                                    @ModelAttribute("params") FormParamMap params) {
        User currentUser = getCurrentUser();
        Squad squad = squadService.getSquadById(squadId, currentUser);

        if (squad == null) {
            model.addAttribute("error", "Specified squad not found :(");
            return SQUAD_VIEW;
        }

        switch (params.getParams().get("command")) {
            case "edit": {
                String idListString = params.getParams().get("idList");
                Set<Long> newSquadShipIds = !idListString.isEmpty() ? Arrays.stream(idListString.split(",")).map(Long::valueOf).collect(Collectors.toSet()) : new HashSet<>();

                String newName = params.getParams().get("name");
                if (!StringUtils.isEmpty(newName)){
                    squad.setName(newName);
                }

                squadService.updateSquad(squad, newSquadShipIds);
                break;
            }
            case "delete": {
                squadService.disbandSquad(squad);
                break;
            }
        }
        return "redirect:/hangar";
    }

    @GetMapping("/select")
    public String getSquadSelectForMission(Model model, @RequestParam("type") Long missionTypeId) {
        User currentUser = getCurrentUser();
        var squads = squadService.getAvailableSquadsForUser(currentUser);
        var missionType = missionTypeRepo.getOne(missionTypeId);

        var summaryList = squads.stream().map(x -> squadService.getSummary(x)).collect(toList());

        model.addAttribute("squads", squads);
        model.addAttribute("summary", summaryList);
        model.addAttribute("missionType", missionType);
        return SQUAD_SELECT_VIEW;
    }

}
