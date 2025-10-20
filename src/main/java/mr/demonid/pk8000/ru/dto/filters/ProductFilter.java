package mr.demonid.pk8000.ru.dto.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Фильтр для выборки товаров из БД.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private Long categoryId;
    private String productName;
    private String description;
}
