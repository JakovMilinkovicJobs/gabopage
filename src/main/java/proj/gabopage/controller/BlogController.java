package proj.gabopage.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proj.gabopage.model.BlogPage;
import proj.gabopage.model.Topic;
import proj.gabopage.service.BlogPageService;
import proj.gabopage.service.TopicService;

@Controller
public class BlogController {

    private final BlogPageService blogPageService;
    private final TopicService topicService;

    public BlogController(BlogPageService blogPageService, TopicService topicService) {
        this.blogPageService = blogPageService;
        this.topicService = topicService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/blog";
    }

    @GetMapping("/blog")
    public String blog(@RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size,
                      Model model) {
        BlogPage blogPage = blogPageService.getOrCreateMainPage();
        Page<Topic> topicsPage = topicService.getTopics(page, size);

        model.addAttribute("page", blogPage);
        model.addAttribute("topics", topicsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", topicsPage.getTotalPages());
        model.addAttribute("hasMore", topicsPage.hasNext());
        model.addAttribute("activePage", "blog");
        return "blog";
    }

    @GetMapping("/blog/profile-image")
    public ResponseEntity<byte[]> profileImage() {
        BlogPage page = blogPageService.getOrCreateMainPage();
        if (page.getProfileImage() == null || page.getProfileImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = page.getProfileImageContentType();
        MediaType mediaType;
        try {
            mediaType = (contentType == null || contentType.isBlank())
                    ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(contentType);
        } catch (Exception ex) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(page.getProfileImage());
    }
}
