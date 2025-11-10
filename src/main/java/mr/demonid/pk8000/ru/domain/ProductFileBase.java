package mr.demonid.pk8000.ru.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


/**
 * Базовый класс для сущностей файлов.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class ProductFileBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(nullable = false)
    private long version = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private SoftEntity product;

    public void incrementVersion() {
        version++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductFileBase obj))
            return false;
        if (id == null || obj.id == null)
            return false;
        return Objects.equals(id, obj.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductFileBase{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", version=" + version +
                '}';
    }
}
