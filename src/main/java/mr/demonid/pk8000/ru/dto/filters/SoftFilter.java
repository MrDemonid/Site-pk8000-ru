package mr.demonid.pk8000.ru.dto.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * Фильтр для выборки товаров из БД.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftFilter {
    private List<Long> categoryId;
    private String searchText;
}
