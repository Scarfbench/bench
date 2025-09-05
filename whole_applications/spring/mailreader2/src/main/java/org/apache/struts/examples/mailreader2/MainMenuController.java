package org.apache.struts.examples.mailreader2;

import org.apache.struts.examples.mailreader2.dao.User;
import org.apache.struts.examples.mailreader2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainMenuController {

    private final UserService userService;

    public MainMenuController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mainMenu")
    public String showMainMenu(Model model) {
        User user = userService.getAuthUser();
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "mainMenu";
    }
}
