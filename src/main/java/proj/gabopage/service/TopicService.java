package proj.gabopage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import proj.gabopage.model.Topic;
import proj.gabopage.repository.TopicRepository;
import proj.gabopage.util.HtmlSanitizer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository repository;
    private final DisplayOrderService displayOrderService;

    public TopicService(TopicRepository repository, DisplayOrderService displayOrderService) {
        this.repository = repository;
        this.displayOrderService = displayOrderService;
    }

    public Page<Topic> getTopics(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.desc("createdAt")));
        return repository.findAll(pageable);
    }

    public List<Topic> getAllTopicsOrdered() {
        return repository.findAll(Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.desc("createdAt")));
    }

    public Optional<Topic> getTopicById(Long id) {
        return repository.findById(id);
    }

    public Topic createTopic(String title, String description, String richHtml, MultipartFile coverImage) throws IOException {
        Topic topic = new Topic();
        topic.setTitle(title);
        topic.setDescription(description);
        topic.setRichHtml(HtmlSanitizer.sanitize(richHtml));
        topic.setDisplayOrder(displayOrderService.nextDisplayOrder(getAllTopicsOrdered()));

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
        List<Topic> topics = getAllTopicsOrdered();
        displayOrderService.normalize(topics);
        repository.saveAll(topics);
    }

    @Transactional
    public void reorderTopics(List<Long> orderedIds) {
        List<Topic> topics = getAllTopicsOrdered();
        displayOrderService.applyOrder(topics, orderedIds);
        repository.saveAll(topics);
    }
}
