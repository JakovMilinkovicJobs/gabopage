package proj.gabopage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Page<Topic> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
