package proj.gabopage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/gabo-secure-admin-2024")
    public String login() {
        return "admin/login";
    }
}