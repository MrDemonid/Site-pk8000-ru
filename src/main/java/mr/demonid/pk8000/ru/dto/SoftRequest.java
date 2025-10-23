package mr.demonid.pk8000.ru.dto;


/**
 * Единица продукта - программа.
 * @param name             Название программы/документа.
 * @param category         Категория.
 * @param shortDescription Короткое описание.
 * @param description      Полное описание.
 */
public record SoftRequest(
            String name,
            Long category,
            String shortDescription,
            String description) {
}
