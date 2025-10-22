package mr.demonid.pk8000.ru.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.CategoryEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
@Log4j2
public class SoftMapper {

    private AppConfiguration config;


    /**
     * Конвертируем сущность в DTO.
     */
    public SoftResponse toResponse(SoftEntity entity) {
        return new SoftResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory().getId(),
                entity.getShortDescription(),
                entity.getDescription(),
                toImageLinks(entity.getId(), entity.getImageFiles()),
                toAttacheLinks(entity.getId(), entity.getImageFiles())
        );
    }

    /**
     * Создает новую сущность.
     */
    public SoftEntity toEntity(SoftRequest response, CategoryEntity categoryEntity) {
        return new SoftEntity(
                response.name(),
                response.shortDescription(),
                response.description(),
                categoryEntity,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }


    /**
     * Конвертирует имена изображений в ссылки.
     */
    private List<String> toImageLinks(Long id, List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(e -> Paths.get("/pk8000/api/catalog/images", id.toString(), e).toString())
                .toList();
    }

    /**
     * Конвертирует имена файлов в ссылки для их загрузки.
     */
    private List<String> toAttacheLinks(Long id, List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(e -> Paths.get("/pk8000/api/catalog/images", id.toString(), e).toString())
                .toList();
    }

}
