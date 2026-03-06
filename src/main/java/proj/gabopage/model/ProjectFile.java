package proj.gabopage.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class ProjectFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;
    private String contentType;
    private long size;

    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BLOB")
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;
}