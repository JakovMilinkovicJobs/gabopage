package proj.gabopage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.ProjectFile;

import java.util.List;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    List<ProjectFile> findByProjectIdOrderByUploadedAtDesc(Long projectId);
}
