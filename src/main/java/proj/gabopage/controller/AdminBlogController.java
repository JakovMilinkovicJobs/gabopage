package proj.gabopage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.model.BlogPage;
import proj.gabopage.repository.BlogPageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/blog")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogController {

    private final BlogPageRepository repo;

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    public AdminBlogController(BlogPageRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/edit")
    public String editForm(Model model) {
        BlogPage page = repo.findById(1L).orElseGet(() -> repo.save(defaultPage()));
        model.addAttribute("page", page);
        return "admin/blog-edit";
    }

    @PostMapping("/edit")
    public String save(@RequestParam("richHtml") String richHtml,
                       @RequestParam("authorName") String authorName,
                       @RequestParam("authorSubtitle") String authorSubtitle,
                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        BlogPage page = repo.findById(1L).orElseGet(this::defaultPage);
        page.setId(1L);
        page.setRichHtml(richHtml);
        page.setAuthorName(authorName);
        page.setAuthorSubtitle(authorSubtitle);

        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = storeProfileImage(profileImage);
            page.setProfileImageUrl(imageUrl);
        }

        repo.save(page);
        return "redirect:/blog";
    }

    private BlogPage defaultPage() {
        BlogPage page = new BlogPage();
        page.setId(1L);
        page.setRichHtml("<p>Welcome to the blog.</p>");
        page.setAuthorName("Gabriel Marvin");
        page.setAuthorSubtitle("Engineer · Builder · Writer");
        return page;
    }

    private String storeProfileImage(MultipartFile file) throws IOException {
        Path profileUploadDir = Paths.get(uploadDir, "profiles");
        Files.createDirectories(profileUploadDir);

        String originalName = file.getOriginalFilename() == null ? "profile" : file.getOriginalFilename();
        String extension = "";
        int idx = originalName.lastIndexOf('.');
        if (idx >= 0) {
            extension = originalName.substring(idx);
        }

        String filename = "profile-" + UUID.randomUUID() + extension;
        Path destination = profileUploadDir.resolve(filename);
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/profiles/" + filename;
    }
}
