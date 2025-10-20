package mr.demonid.pk8000.ru.dto;

import java.util.List;


/**
 * Запрос на добавление товара в БД.
 */
public record ProductRequest (
    String name,
    String shortDescription,
    String description,
    Long categoryId,
    List<String> imageFiles,
    List<String> archiveFiles)
{
}
