package proj.gabopage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proj.gabopage.model.Link;
import proj.gabopage.repository.LinkRepository;

import java.util.Optional;

@Service
public class LinkService {

    private final LinkRepository repository;

    public LinkService(LinkRepository repository) {
        this.repository = repository;
    }

    public Page<Link> getLinks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Optional<Link> getLinkById(Long id) {
        return repository.findById(id);
    }

    public Link createLink(String url, String description) {
        Link link = new Link();
        link.setTitle(null);
        link.setUrl(url);
        link.setDescription(description);
        return repository.save(link);
    }

    public Link updateLink(Long id, String url, String description) {
        Link link = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Link not found"));

        link.setTitle(null);
        link.setUrl(url);
        link.setDescription(description);

        return repository.save(link);
    }

    public void deleteLink(Long id) {
        repository.deleteById(id);
    }
}
