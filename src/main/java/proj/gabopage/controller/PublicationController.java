package proj.gabopage.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proj.gabopage.model.Publication;
import proj.gabopage.service.PublicationService;

@Controller
public class PublicationController {

    private final PublicationService publicationService;

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @GetMapping("/publications")
    public String publications(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "20") int size,
                              Model model) {
        Page<Publication> publicationsPage = publicationService.getPublications(page, size);

        model.addAttribute("publications", publicationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", publicationsPage.getTotalPages());
        model.addAttribute("hasMore", publicationsPage.hasNext());
        model.addAttribute("activePage", "publications");
        return "user/publications";
    }
}
