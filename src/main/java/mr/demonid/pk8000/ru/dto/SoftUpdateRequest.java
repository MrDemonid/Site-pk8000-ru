package mr.demonid.pk8000.ru.dto;


/**
 * Запрос на обновление продукта.
 * @param id               Идентификатор продукта.
 * @param name             Название программы.
 * @param category         Категория.
 * @param shortDescription Короткое описание.
 */
public record SoftUpdateRequest (
        Long id,
        String name,
        Long category,
        String shortDescription) {
}
