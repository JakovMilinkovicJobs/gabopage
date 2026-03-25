package proj.gabopage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
    Page<Publication> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
