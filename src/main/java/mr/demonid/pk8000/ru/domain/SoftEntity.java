package mr.demonid.pk8000.ru.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "products")
public class SoftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(name = "short_description", length = 256)
    private String shortDescription;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_file")
    private List<String> imageFiles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_archives", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "archive_file")
    private List<String> archiveFiles;

    @Column(name = "create_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;


    public SoftEntity(String name, String shortDescription, CategoryEntity category, List<String> newImageFiles, List<String> newArchiveFiles) {
        this.id = null;
        this.name = name;
        this.shortDescription = shortDescription;
        this.category = category;
        this.imageFiles = newImageFiles == null ? new ArrayList<>() : newImageFiles;
        this.archiveFiles = newArchiveFiles == null ? new ArrayList<>() : newArchiveFiles;
    }


    public SoftEntity() {
        this(null, null, null, new ArrayList<>(), null);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SoftEntity softEntity))
            return false;
        if (id == null || softEntity.id == null)
            return false;
        return Objects.equals(id, softEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
