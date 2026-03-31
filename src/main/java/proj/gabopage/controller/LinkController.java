package proj.gabopage.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proj.gabopage.model.Link;
import proj.gabopage.service.LinkService;

@Controller
public class LinkController {

    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/links")
    public String links(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "20") int size,
                       Model model) {
        Page<Link> linksPage = linkService.getLinks(page, size);

        model.addAttribute("links", linksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", linksPage.getTotalPages());
        model.addAttribute("hasMore", linksPage.hasNext());
        model.addAttribute("activePage", "links");
        return "user/links";
    }
}
