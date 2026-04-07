package proj.gabopage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import proj.gabopage.controller.dto.ReorderRequest;
import proj.gabopage.model.Link;
import proj.gabopage.service.LinkService;

@Controller
@RequestMapping("/admin/links")
@PreAuthorize("hasRole('ADMIN')")
public class AdminLinkController {

    private final LinkService linkService;

    public AdminLinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/list")
    public String manageLinks(Model model) {
        model.addAttribute("links", linkService.getAllLinksOrdered());
        model.addAttribute("activePage", "links");
        return "admin/links-list";
    }

    @GetMapping("/new")
    public String newLinkForm(Model model) {
        model.addAttribute("link", new Link());
        model.addAttribute("activePage", "links");
        return "admin/link-edit";
    }

    @PostMapping("/new")
    public String createLink(@RequestParam("url") String url,
                             @RequestParam("description") String description) {
        linkService.createLink(url, description);
        return "redirect:/admin/links/list";
    }

    @GetMapping("/{id}/edit")
    public String editLinkForm(@PathVariable Long id, Model model) {
        Link link = linkService.getLinkById(id)
                .orElseThrow(() -> new IllegalArgumentException("Link not found"));
        model.addAttribute("link", link);
        model.addAttribute("activePage", "links");
        return "admin/link-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateLink(@PathVariable Long id,
                             @RequestParam("url") String url,
                             @RequestParam("description") String description) {
        linkService.updateLink(id, url, description);
        return "redirect:/admin/links/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteLink(@PathVariable Long id) {
        linkService.deleteLink(id);
        return "redirect:/admin/links/list";
    }

    @PostMapping("/reorder")
    @ResponseBody
    public void reorderLinks(@RequestBody ReorderRequest request) {
        linkService.reorderLinks(request.orderedIds());
    }
}
