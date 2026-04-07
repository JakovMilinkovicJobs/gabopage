package proj.gabopage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
