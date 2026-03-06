package proj.gabopage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import proj.gabopage.repository.ProjectRepository;

import java.util.Objects;

@Controller
public class HomeController {

    private final ProjectRepository projectRepository;

    public HomeController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        if (auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"))) {
            return "redirect:admin/dashboard";
        }

        model.addAttribute("projects", projectRepository.findAll());
        return "index";
    }
}
