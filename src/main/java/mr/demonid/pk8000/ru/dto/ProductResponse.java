package mr.demonid.pk8000.ru.dto;

import java.util.List;


/**
 * Единица продукта.
 * @param id               ID.
 * @param name             Название программы/документа.
 * @param category         Категория.
 * @param shortDescription Короткое описание.
 * @param description      Полное описание.
 * @param imageUrls        Список ссылок на сам продукт.
 * @param archiveUrl       Ссылка на архив с программой.
 */
public record ProductResponse(
        Long id,
        String name,
        Long category,
        String shortDescription,
        String description,
        List<String> imageUrls,
        String archiveUrl) {
}
