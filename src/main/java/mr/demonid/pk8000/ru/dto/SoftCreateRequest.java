package mr.demonid.pk8000.ru.dto;


/**
 * Единица продукта - программа.
 * @param name             Название программы/документа.
 * @param category         Категория.
 * @param shortDescription Короткое описание.
 */
public record SoftCreateRequest(
            String name,
            Long category,
            String shortDescription) {
}
