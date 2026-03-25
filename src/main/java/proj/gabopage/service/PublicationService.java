package proj.gabopage.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import proj.gabopage.model.Publication;
import proj.gabopage.repository.PublicationRepository;

import java.util.Optional;

@Service
public class PublicationService {

    private final PublicationRepository repository;

    public PublicationService(PublicationRepository repository) {
        this.repository = repository;
    }

    public Page<Publication> getPublications(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Optional<Publication> getPublicationById(Long id) {
        return repository.findById(id);
    }

    public Publication createPublication(String title, String url) {
        Publication publication = new Publication();
        publication.setTitle(title);
        publication.setUrl(url);
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
    }
}
