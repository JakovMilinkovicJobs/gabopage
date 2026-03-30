package proj.gabopage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.model.Topic;
import proj.gabopage.repository.TopicRepository;
import proj.gabopage.util.HtmlSanitizer;

import java.io.IOException;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository repository;

    public TopicService(TopicRepository repository) {
        this.repository = repository;
    }

    public Page<Topic> getTopics(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Optional<Topic> getTopicById(Long id) {
        return repository.findById(id);
    }

    public Topic createTopic(String title, String description, String richHtml, MultipartFile coverImage) throws IOException {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setRichHtml(HtmlSanitizer.sanitize(richHtml));

        if (coverImage != null && !coverImage.isEmpty()) {
            topic.setCoverImage(coverImage.getBytes());
            topic.setCoverImageContentType(coverImage.getContentType());
        }

        return repository.save(topic);
    }

    public Topic updateTopic(Long id, String title, String description, String richHtml,
                            MultipartFile coverImage, boolean removeCoverImage) throws IOException {
        Topic topic = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found"));

        topic.setTitle(title);
        topic.setDescription(description);
        topic.setRichHtml(HtmlSanitizer.sanitize(richHtml));

        if (removeCoverImage) {
            topic.setCoverImage(null);
            topic.setCoverImageContentType(null);
        } else if (coverImage != null && !coverImage.isEmpty()) {
            topic.setCoverImage(coverImage.getBytes());
            topic.setCoverImageContentType(coverImage.getContentType());
        }

        return repository.save(topic);
    }

    public void deleteTopic(Long id) {
        repository.deleteById(id);
    }
}
