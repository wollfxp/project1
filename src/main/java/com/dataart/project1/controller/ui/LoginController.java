package com.dataart.project1.controller.ui;

import com.dataart.project1.entity.SecurityUser;
import com.dataart.project1.entity.User;
import com.dataart.project1.entity.dto.LoginUserDto;
import com.dataart.project1.entity.dto.RegisterUserDto;
import com.dataart.project1.service.UserDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @Value("${app.whitelabel.name:#{null}}")
    private String whitelabelString;

    @Value("${APP_POD_NAME:#{null}}")
    private String podName;

    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserDataService userDataService;

    @Autowired
    public LoginController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @GetMapping("/login")
    public String getLoginView(@RequestParam(value = "error", required = false) Boolean isError,
                               Model model) {

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            logger.info("redirecting get login for already logged in user");
            return "redirect:/";
        }

        if (isError != null && isError) {
            model.addAttribute("error", true);
            logger.warn("login error tracked");
        }
        model.addAttribute("user", new LoginUserDto());
        model.addAttribute("whitelabel", whitelabelString != null ? whitelabelString : "NO_BRAND");
        model.addAttribute("podname", podName != null ? podName : "UNKNOWN_HOST");
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterView(Model model) {
        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            logger.info("redirecting get register for already logged in user");
            return "redirect:/";
        }
        model.addAttribute("user", new RegisterUserDto());
        return "register";
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public String postRegisterForm(RegisterUserDto user, Model model, HttpServletRequest request)
            throws ServletException {

        if (!SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
            logger.info("redirecting post register for already logged in user");
            return "redirect:/";
        }

        logger.info("registering " + user.getUsername());
        User newUser = userDataService.register(user);
        logger.info("done registering " + user.getUsername());

        if (newUser != null) {
            SecurityUser loggedInUser = userDataService.tryLogin(user.getUsername(), user.getPassword());
            request.login(loggedInUser.getUsername(), loggedInUser.getPassword());
            return "redirect:/";
        } else {
            logger.error("failed to register user!");
            model.addAttribute("error", "something's wrong!");
            return "register";
        }
    }

}
