package proj.gabopage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.service.BlogPageService;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogController {

    private final BlogPageService blogPageService;

    public AdminBlogController(BlogPageService blogPageService) {
        this.blogPageService = blogPageService;
    }

    @GetMapping("/blog/edit")
    public String editForm(Model model) {
        model.addAttribute("page", blogPageService.getOrCreateMainPage());
        model.addAttribute("activePage", "blog");
        return "admin/blog-edit";
    }

    @PostMapping("/blog/edit")
    public String save(@RequestParam("richHtml") String richHtml,
                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                       @RequestParam(value = "removeProfileImage", defaultValue = "false") boolean removeProfileImage) throws IOException {
        blogPageService.saveMainPage(richHtml, profileImage, removeProfileImage);
        return "redirect:/blog";
    }
}
