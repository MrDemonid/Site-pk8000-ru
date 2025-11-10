package mr.demonid.pk8000.ru.dto;

import java.util.List;
import java.util.Map;


/**
 * Запрос на изменение описания продукта.
 * @param removed Список удаляемых файлов (аттачей в md-файле).
 * @param replaced Список заменяемых файлов.
 */
public record DescriptionMetaRequest(
        List<String> removed,
        Map<String, String> replaced
) {
}