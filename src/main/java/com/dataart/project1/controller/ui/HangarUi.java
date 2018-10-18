package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.Starship;
import com.dataart.project1.entity.dto.FormParamMap;
import com.dataart.project1.repo.SquadRepo;
import com.dataart.project1.repo.StarshipRepo;
import com.dataart.project1.repo.StarshipTypeRepo;
import com.dataart.project1.service.game.HangarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;

@Controller
@RequestMapping("/hangar")
@PreAuthorize("isFullyAuthenticated()")
public class HangarUi extends UserAwareController {

    private static Logger logger = LoggerFactory.getLogger(HangarUi.class);


    private final static String HANGAR_PAGE = "hangar";
    private final static String HANGAR_PAGE_REDIRECT = "redirect:/hangar";
    private static final String REPAIR = "repair";

    private StarshipRepo starshipRepo;
    private SquadRepo squadRepo;
    private HangarService hangarService;
    private StarshipTypeRepo starshipTypeRepo;

    @Autowired
    public HangarUi(StarshipRepo starshipRepo, SquadRepo squadRepo, HangarService hangarService, StarshipTypeRepo starshipTypeRepo) {
        this.starshipRepo = starshipRepo;
        this.squadRepo = squadRepo;
        this.hangarService = hangarService;
        this.starshipTypeRepo = starshipTypeRepo;
    }

    @GetMapping
    public String getHangarView(Model model) {
        var currentUser = getCurrentUser();
        var ships = starshipRepo.findAllByOwner(currentUser);
        ships.sort(Comparator.comparing(x -> x.getSquad() == null));
        var shipTypes = starshipTypeRepo.findByIsEnemyOnlyFalse();

        model.addAttribute("repairCost", hangarService.getRepairCostAll(ships));
        model.addAttribute("ships", ships);
        model.addAttribute("user", currentUser);
        model.addAttribute("shipTypes", shipTypes);
        model.addAttribute("hangarUpgradeCreditCost", hangarService.getUpgradeCreditCost(currentUser));
        model.addAttribute("hangarUpgradeCoreCost", hangarService.getUpgradeCoreCost(currentUser));
        model.addAttribute("squads", squadRepo.findAllByOwner(currentUser));
        return HANGAR_PAGE;
    }

    @PostMapping("/repair")
    public String postRepairShips(@ModelAttribute FormParamMap params) {
        var currentUser = getCurrentUser();
        var action = params.getParams().get("action");

        var ships = starshipRepo.findAllByOwner(currentUser);
        if (currentUser.getCredits().compareTo(hangarService.getRepairCostAll(ships)) < 0) {
            return HANGAR_PAGE_REDIRECT;
        }

        if (action.equals(REPAIR)) {
            for (Starship x : ships) {
                if (x.getHealth() < x.getType().getHealth()
                        && (x.getSquad() != null && x.getSquad().getActiveMission() == null)) {
                    hangarService.repairShip(x, currentUser);
                }
            }
        }

        return HANGAR_PAGE_REDIRECT;
    }


    @PostMapping("/build")
    @PreAuthorize("hasAuthority('USER')")
    public String postGiveShipForm(@ModelAttribute FormParamMap cmdContainer) {
        String shipType = cmdContainer.getParams().get("shipType");
        hangarService.buildShip(shipType, getCurrentUser());
        return HANGAR_PAGE_REDIRECT;
    }

    @PostMapping("/upgrade")
    @PreAuthorize("hasAuthority('USER')")
    public String postUpgradeHangarForm(@ModelAttribute FormParamMap cmdContainer) {
        String cmd = cmdContainer.getParams().get("action");
        if (cmd.equals("upgrade")){
            hangarService.upgradeHangar(getCurrentUser());
        }
        return HANGAR_PAGE_REDIRECT;
    }

}
