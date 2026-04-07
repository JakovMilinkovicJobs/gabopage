package proj.gabopage.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.controller.dto.ReorderRequest;
import proj.gabopage.model.Topic;
import proj.gabopage.service.TopicService;

import java.io.IOException;

@Controller
@RequestMapping("/admin/topics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTopicController {

    private final TopicService topicService;

    public AdminTopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/list")
    public String manageTopics(Model model) {
        model.addAttribute("topics", topicService.getAllTopicsOrdered());
        model.addAttribute("activePage", "blog");
        return "admin/topics-list";
    }

    // New topic
    @GetMapping("/new")
    public String newTopicForm(Model model) {
        model.addAttribute("topic", new Topic());
        model.addAttribute("activePage", "blog");
        return "admin/topic-edit";
    }

    @PostMapping("/new")
    public String createTopic(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("richHtml") String richHtml,
                             @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) throws IOException {
        topicService.createTopic(title, description, richHtml, coverImage);
        return "redirect:/admin/topics/list";
    }

    // Edit topic
    @GetMapping("/{id}/edit")
    public String editTopicForm(@PathVariable Long id, Model model) {
        Topic topic = topicService.getTopicById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));
        model.addAttribute("topic", topic);
        model.addAttribute("activePage", "blog");
        return "admin/topic-edit";
    }

    @PostMapping("/{id}/edit")
    public String updateTopic(@PathVariable Long id,
                             @RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("richHtml") String richHtml,
                             @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
                             @RequestParam(value = "removeCoverImage", defaultValue = "false") boolean removeCoverImage) throws IOException {
        topicService.updateTopic(id, title, description, richHtml, coverImage, removeCoverImage);
        return "redirect:/admin/topics/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return "redirect:/admin/topics/list";
    }

    @PostMapping("/reorder")
    @ResponseBody
    public void reorderTopics(@RequestBody ReorderRequest request) {
        topicService.reorderTopics(request.orderedIds());
    }
}
