package proj.gabopage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proj.gabopage.model.BlogPage;
import proj.gabopage.repository.BlogPageRepository;

@Controller
public class BlogController {

    private final BlogPageRepository repo;

    public BlogController(BlogPageRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        BlogPage page = repo.findById(1L).orElseGet(() -> {
            BlogPage p = new BlogPage();
            p.setRichHtml("<p>Welcome to the blog.</p>");
            return repo.save(p);
        });

        model.addAttribute("page", page);
        model.addAttribute("activePage", "blog");
        return "blog";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/blog/edit")
    public String editBlog(Model model) {
        BlogPage page = repo.findById(1L).orElseGet(() -> {
            BlogPage p = new BlogPage();
            p.setId(1L);
            p.setRichHtml("<p>Welcome to the blog.</p>");
            return repo.save(p);
        });

        model.addAttribute("page", page);
        model.addAttribute("activePage", "blog");
        return "admin/blog-edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/blog/edit")
    public String saveBlog(@RequestParam("richHtml") String richHtml) {
        BlogPage page = repo.findById(1L).orElseGet(BlogPage::new);
        page.setId(1L);
        page.setRichHtml(richHtml);
        repo.save(page);
        return "redirect:/blog";
    }
}
