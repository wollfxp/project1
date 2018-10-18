package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.User;
import com.dataart.project1.entity.dto.AdminCommand;
import com.dataart.project1.entity.dto.AdminCommandContainer;
import com.dataart.project1.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminUi extends UserAwareController {

    private static final String ADMIN_UI = "admin";
    private AdminService adminService;

    @Autowired
    public AdminUi(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdminUi(Model model) {
        model.addAttribute("cmdContainer", new AdminCommandContainer(AdminCommand.GIVE_SHIP));
        return ADMIN_UI;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String postGiveShipForm(@ModelAttribute AdminCommandContainer cmdContainer, Model model) {
        String loggedInUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepo.findByName(loggedInUsername);

        if (!cmdContainer.getCommand().equals(AdminCommand.GIVE_SHIP)) {
            model.addAttribute("error", "unknown command " + cmdContainer.getCommand().toString());
            return "error";
        }

        String shipType = cmdContainer.getParams().get("shipType");

        if (user != null) {
            boolean done = adminService.giveShipToUser(shipType, user);
            if (done) {
                model.addAttribute("status", "Given " + shipType + " to " + loggedInUsername);
                model.addAttribute("cmdContainer", new AdminCommandContainer(AdminCommand.GIVE_SHIP));
            } else {
                model.addAttribute("status", "Failed to give " + shipType + " to " + loggedInUsername);
                model.addAttribute("error", "true");
                model.addAttribute("cmdContainer", new AdminCommandContainer(AdminCommand.GIVE_SHIP));
            }
        }
        return ADMIN_UI;
    }

}
