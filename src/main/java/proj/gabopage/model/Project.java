package proj.gabopage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "PROJECT")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String shortDescription;

    @Column(length = 5000)
    private String content;

    private String imageUrl;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectFile> files = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "TEXT")
    private String richContentHtml;
}
