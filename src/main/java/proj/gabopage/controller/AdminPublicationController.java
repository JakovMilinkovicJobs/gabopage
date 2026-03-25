package proj.gabopage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proj.gabopage.model.Publication;
import proj.gabopage.service.PublicationService;

@Controller
@RequestMapping("/admin/publications")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPublicationController {

    private final PublicationService publicationService;

    public AdminPublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping("/list")
    public String managePublications(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     Model model) {
        model.addAttribute("publicationsPage", publicationService.getPublications(page, size));
        model.addAttribute("activePage", "publications");
        return "admin/publications-list";
    }

    @GetMapping("/new")
    public String newPublicationForm(Model model) {
        model.addAttribute("publication", new Publication());
        model.addAttribute("activePage", "publications");
        return "admin/publication-edit";
    }

    @PostMapping("/new")
    public String createPublication(@RequestParam("title") String title,
                                   @RequestParam("url") String url) {
        publicationService.createPublication(title, url);
        return "redirect:/admin/publications/list";
    }

    @GetMapping("/{id}/edit")
    public String editPublicationForm(@PathVariable Long id, Model model) {
        Publication publication = publicationService.getPublicationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));
        model.addAttribute("publication", publication);
        model.addAttribute("activePage", "publications");
        return "admin/publication-edit";
    }

    @PostMapping("/{id}/edit")
    public String updatePublication(@PathVariable Long id,
                                   @RequestParam("title") String title,
                                   @RequestParam("url") String url) {
        publicationService.updatePublication(id, title, url);
        return "redirect:/admin/publications/list";
    }

    @PostMapping("/{id}/delete")
    public String deletePublication(@PathVariable Long id) {
        publicationService.deletePublication(id);
        return "redirect:/admin/publications/list";
    }
}
