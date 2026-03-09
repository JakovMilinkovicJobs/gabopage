package proj.gabopage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
        BlogPage page = repo.findById(1L).orElseGet(() -> repo.save(defaultPage()));

        model.addAttribute("page", page);
        model.addAttribute("activePage", "blog");
        return "blog";
    }

    private BlogPage defaultPage() {
        BlogPage p = new BlogPage();
        p.setId(1L);
        p.setRichHtml("<p>Welcome to the blog.</p>");
        p.setAuthorName("Gabriel Marvin");
        p.setAuthorSubtitle("Engineer · Builder · Writer");
        return p;
    }
}
