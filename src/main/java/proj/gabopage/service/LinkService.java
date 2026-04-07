package proj.gabopage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.gabopage.model.Link;
import proj.gabopage.repository.LinkRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {

    private final LinkRepository repository;
    private final DisplayOrderService displayOrderService;

    public LinkService(LinkRepository repository, DisplayOrderService displayOrderService) {
        this.repository = repository;
        this.displayOrderService = displayOrderService;
    }

    public Page<Link> getLinks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.desc("createdAt")));
        return repository.findAll(pageable);
    }

    public List<Link> getAllLinksOrdered() {
        return repository.findAll(Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.desc("createdAt")));
    }

    public Optional<Link> getLinkById(Long id) {
        return repository.findById(id);
    }

    public Link createLink(String url, String description) {
        Link link = new Link();
        link.setTitle(null);
        link.setUrl(url);
        link.setDescription(description);
        link.setDisplayOrder(displayOrderService.nextDisplayOrder(getAllLinksOrdered()));
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
        List<Link> links = getAllLinksOrdered();
        displayOrderService.normalize(links);
        repository.saveAll(links);
    }

    @Transactional
    public void reorderLinks(List<Long> orderedIds) {
        List<Link> links = getAllLinksOrdered();
        displayOrderService.applyOrder(links, orderedIds);
        repository.saveAll(links);
    }
}
