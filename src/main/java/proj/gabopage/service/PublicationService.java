package proj.gabopage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.gabopage.model.Publication;
import proj.gabopage.repository.PublicationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PublicationService {

    private final PublicationRepository repository;
    private final DisplayOrderService displayOrderService;

    public PublicationService(PublicationRepository repository, DisplayOrderService displayOrderService) {
        this.repository = repository;
        this.displayOrderService = displayOrderService;
    }

    public Page<Publication> getPublications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.desc("createdAt")));
        return repository.findAll(pageable);
    }

    public List<Publication> getAllPublicationsOrdered() {
        return repository.findAll(Sort.by(
                Sort.Order.asc("displayOrder"),
                Sort.Order.desc("createdAt")));
    }

    public Optional<Publication> getPublicationById(Long id) {
        return repository.findById(id);
    }

    public Publication createPublication(String title, String url) {
        Publication publication = new Publication();
        publication.setTitle(title);
        publication.setUrl(url);
        publication.setDisplayOrder(displayOrderService.nextDisplayOrder(getAllPublicationsOrdered()));
        return repository.save(publication);
    }

    public Publication updatePublication(Long id, String title, String url) {
        Publication publication = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication not found"));

        publication.setTitle(title);
        publication.setUrl(url);

        return repository.save(publication);
    }

    public void deletePublication(Long id) {
        repository.deleteById(id);
        List<Publication> publications = getAllPublicationsOrdered();
        displayOrderService.normalize(publications);
        repository.saveAll(publications);
    }

    @Transactional
    public void reorderPublications(List<Long> orderedIds) {
        List<Publication> publications = getAllPublicationsOrdered();
        displayOrderService.applyOrder(publications, orderedIds);
        repository.saveAll(publications);
    }
}
