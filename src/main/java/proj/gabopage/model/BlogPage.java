package proj.gabopage.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "BLOG_PAGE")
public class BlogPage {

    @Id
    private Long id = 1L;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String richHtml;

    private String authorName;

    private String authorSubtitle;

    private String profileImageUrl;
}