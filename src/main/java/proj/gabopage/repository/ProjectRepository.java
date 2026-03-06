package proj.gabopage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
