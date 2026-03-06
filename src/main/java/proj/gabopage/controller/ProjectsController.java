package proj.gabopage.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proj.gabopage.model.Project;
import proj.gabopage.repository.ProjectRepository;

@Controller
public class ProjectsController {

    private final ProjectRepository projectRepository;

    public ProjectsController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    public String projects(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Project> projectPage = projectRepository.findAll(pageable);

        model.addAttribute("projectPage", projectPage);
        model.addAttribute("size", size);

        return "projects";
    }
}