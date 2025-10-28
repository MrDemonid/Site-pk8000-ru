package mr.demonid.pk8000.ru.dto;

import java.util.List;


/**
 * Единица продукта - программа.
 * @param id               ID.
 * @param name             Название программы/документа.
 * @param category         Категория.
 * @param shortDescription Краткое описание.
 * @param imageUrls        Список ссылок на сам продукт.
 * @param archiveUrls      Ссылка на архив с программой.
 */
public record SoftResponse(
        Long id,
        String name,
        Long category,
        String shortDescription,
        List<String> imageUrls,
        List<String> archiveUrls) {
}
