package proj.gabopage.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proj.gabopage.model.Topic;
import proj.gabopage.service.TopicService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/api/topics")
    @ResponseBody
    public Map<String, Object> getTopicsApi(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "5") int size) {
        Page<Topic> topicsPage = topicService.getTopics(page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("topics", topicsPage.getContent());
        response.put("currentPage", page);
        response.put("totalPages", topicsPage.getTotalPages());
        response.put("hasMore", topicsPage.hasNext());

        return response;
    }

    @GetMapping("/blog/topics/{id}")
    public String topicDetail(@PathVariable Long id, Model model) {
        Topic topic = topicService.getTopicById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        model.addAttribute("topic", topic);
        model.addAttribute("activePage", "blog");
        return "user/topic-detail";
    }

    @GetMapping("/blog/topics/{id}/cover-image")
    public ResponseEntity<byte[]> coverImage(@PathVariable Long id) {
        Topic topic = topicService.getTopicById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        if (topic.getCoverImage() == null || topic.getCoverImage().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = topic.getCoverImageContentType();
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
                .body(topic.getCoverImage());
    }
}
