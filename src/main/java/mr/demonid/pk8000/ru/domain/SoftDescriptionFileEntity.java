package mr.demonid.pk8000.ru.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Внешний файл для поля description у SoftEntity.
 */
@Entity
@Getter
@Setter
@Table(name = "product_description_files")
public class SoftDescriptionFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    @ToString.Exclude
    private SoftEntity product;

    @Lob
    @Column(name = "description_cache")
    private String description;

    @Column(name = "file_name", length = 256, nullable = false)
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_created_at")
    private Long fileCreatedAt;

    @Column(name = "file_modified_at")
    private Long fileModifiedAt;

    @Override
    public String toString() {
        return "SoftDescriptionFileEntity{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileCreatedAt=" + fileCreatedAt +
                ", fileModifiedAt=" + fileModifiedAt +
                '}';
    }

}
