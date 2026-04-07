package proj.gabopage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
