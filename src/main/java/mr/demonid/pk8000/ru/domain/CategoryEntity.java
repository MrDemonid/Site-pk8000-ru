package mr.demonid.pk8000.ru.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "categories")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = false, nullable = false, length = 128)
    private String name;

    @Column(unique = false, nullable = true, length = 512)
    private String description;

    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public CategoryEntity(String name) {
        this(name, null);
    }

}
