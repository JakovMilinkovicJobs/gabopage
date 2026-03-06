package proj.gabopage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import proj.gabopage.model.BlogPage;

public interface BlogPageRepository extends JpaRepository<BlogPage, Long> { }