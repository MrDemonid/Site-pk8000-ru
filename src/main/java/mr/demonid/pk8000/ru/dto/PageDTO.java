package mr.demonid.pk8000.ru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;


/**
 * Кастомный класс Page, поскольку Spring Boot выдает предупреждение
 * о нестабильном классе PageImpl.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public PageDTO(Page<T> page) {
        page.getContent();
        this.content = page.getContent();
        this.totalPages = Math.max(page.getTotalPages(), 0);
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.number = Math.max(page.getNumber(), 0);
    }

    public static <T> PageDTO<T> empty() {
        return new PageDTO<>(
                Collections.emptyList(), 0, 0, 0, 0
        );
    }

}

