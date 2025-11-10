package mr.demonid.pk8000.ru.dto;

import java.util.List;


/**
 * Ответ клиенту о составе описания.
 * @param id    Идентификатор сущности SoftDescriptionFileEntity.
 * @param files Список файлов описателя, с относительными путями.
 */
public record DescriptionResponse(
        Long id,
        List<String> files) {
}
