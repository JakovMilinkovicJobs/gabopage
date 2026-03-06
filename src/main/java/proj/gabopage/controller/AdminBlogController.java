package proj.gabopage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proj.gabopage.model.BlogPage;
import proj.gabopage.repository.BlogPageRepository;

@Controller
@RequestMapping("/admin/blog")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogController {

    private final BlogPageRepository repo;

    public AdminBlogController(BlogPageRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/edit")
    public String editForm(Model model) {
        BlogPage page = repo.findById(1L).orElseGet(() -> repo.save(new BlogPage()));
        model.addAttribute("page", page);
        return "admin/blog-edit";
    }

    @PostMapping("/edit")
    public String save(@RequestParam("richHtml") String richHtml) {
        BlogPage page = repo.findById(1L).orElseGet(() -> new BlogPage());
        page.setId(1L);
        page.setRichHtml(richHtml);
        repo.save(page);
        return "redirect:/blog";
    }
}