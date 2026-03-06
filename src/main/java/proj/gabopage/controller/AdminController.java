package proj.gabopage.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.model.Project;
import proj.gabopage.model.ProjectFile;
import proj.gabopage.repository.ProjectRepository;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProjectRepository projectRepository;

    public AdminController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<Project> projectPage = projectRepository.findAll(pageable);

        model.addAttribute("projectPage", projectPage);
        model.addAttribute("size", size);

        return "admin/dashboard";
    }

    // NEW (GET)
    @GetMapping("/projects/new")
    public String newProjectForm(Model model) {
        Project p = new Project();
        p.setCreatedAt(LocalDate.now()); // optional default
        model.addAttribute("project", p);
        return "admin/new-project";
    }

    // NEW (POST)
    @PostMapping("/projects/new")
    public String createProject(@ModelAttribute("project") Project form,
                                @RequestParam(value = "newFiles", required = false) MultipartFile[] files) throws Exception {

        form.setId(null); // ensure generated
        if (form.getCreatedAt() == null) {
            form.setCreatedAt(LocalDate.now());
        }

        // save first to get an ID
        Project saved = projectRepository.save(form);

        if (files != null) {
            saveFiles(files, saved);
            projectRepository.save(saved);
        }

        return "redirect:/admin/dashboard";
    }

    // EDIT (GET)
    @GetMapping("/projects/{id}/edit")
    public String editProjectForm(@PathVariable Long id, Model model) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));
        model.addAttribute("project", existing);
        return "admin/edit-project";
    }

    // EDIT (POST)
    @PostMapping("/projects/{id}/edit")
    public String updateProject(@PathVariable Long id,
                                @ModelAttribute("project") Project form,
                                @RequestParam(value = "newFiles", required = false) MultipartFile[] files) throws Exception {

        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found: " + id));

        existing.setTitle(form.getTitle());
        existing.setShortDescription(form.getShortDescription());
        existing.setContent(form.getContent());
        existing.setImageUrl(form.getImageUrl());
        existing.setCreatedAt(form.getCreatedAt());

        if (files != null) {
            saveFiles(files, existing);
        }

        projectRepository.save(existing);
        return "redirect:/admin/dashboard";
    }

    private void saveFiles(@RequestParam(value = "files", required = false) MultipartFile[] files, Project existing) throws IOException {
        for (MultipartFile f : files) {
            if (f.isEmpty()) continue;

            ProjectFile pf = new ProjectFile();
            pf.setProject(existing);
            pf.setOriginalFilename(f.getOriginalFilename());
            pf.setContentType(f.getContentType());
            pf.setSize(f.getSize());
            pf.setData(f.getBytes());

            existing.getFiles().add(pf);
        }
    }

    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}