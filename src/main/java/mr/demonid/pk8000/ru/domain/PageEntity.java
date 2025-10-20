package mr.demonid.pk8000.ru.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * Метаданные страницы (фрагмента).
 */
@Entity
@Getter
@Setter
@ToString
@Table(name = "pages")
public class PageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 32)
    private String slug;

    @Column(nullable = false, length = 128)
    private String title;

    @Column(name = "category_id", nullable = false, length = 32)
    private String categoryId;

    @Column(name = "file_path", nullable = false, length = 512)
    private String filePath;

    @Column(name = "create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "update_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime updatedAt;


    public PageEntity(String slug, String title, String categoryId, String filePath) {
        this.id = null;
        this.slug = slug;
        this.title = title;
        this.categoryId = categoryId;
        this.filePath = filePath;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public PageEntity() {
        this(null, null, null, null);
    }

}

