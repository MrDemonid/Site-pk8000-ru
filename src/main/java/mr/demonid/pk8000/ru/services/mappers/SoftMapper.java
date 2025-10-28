package mr.demonid.pk8000.ru.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.CategoryEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.services.staticpage.MarkdownService;
import mr.demonid.pk8000.ru.util.PathUtil;
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
    private MarkdownService markdownService;


    /**
     * Конвертируем сущность в DTO.
     */
    public SoftResponse toResponse(SoftEntity entity) {
        return new SoftResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory().getId(),
                entity.getShortDescription(),
                markdownService.toHtml(entity.getDescription(), config.getDescDirectory()),
                toImageLinks(entity.getImageFiles()),
                toAttacheLinks(entity.getArchiveFiles())
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
    private List<String> toImageLinks(List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(e -> PathUtil.normalize(Paths.get(config.getSoftImagesUrl(), e).toString(), true))
                .toList();
    }

    /**
     * Конвертирует имена файлов в ссылки для их загрузки.
     */
    private List<String> toAttacheLinks(List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(e -> PathUtil.normalize(Paths.get(config.getSoftFilesUrl(), e).toString(), true))
                .toList();
    }

}

