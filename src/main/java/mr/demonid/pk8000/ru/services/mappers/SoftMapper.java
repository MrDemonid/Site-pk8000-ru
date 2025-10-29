package mr.demonid.pk8000.ru.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AliasPaths;
import mr.demonid.pk8000.ru.domain.CategoryEntity;
import mr.demonid.pk8000.ru.domain.SoftDescriptionFileEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.services.markdown.MarkdownService;
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

    private final MarkdownService markdownService;
    private AliasPaths aliasPaths;


    /**
     * Конвертируем сущность в DTO.
     */
    public SoftResponse toResponse(SoftEntity entity) {
        return new SoftResponse(
                entity.getId(),
                entity.getName(),
                entity.getCategory().getId(),
                entity.getShortDescription(),
                toFilesLinks(entity.getImageFiles()),
                toFilesLinks(entity.getArchiveFiles())
        );
    }

    /**
     * Создает новую сущность.
     */
    public SoftEntity toEntity(SoftRequest response, CategoryEntity categoryEntity) {
        return new SoftEntity(
                response.name(),
                response.shortDescription(),
                categoryEntity,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public String descriptionToHtml(SoftDescriptionFileEntity entity) {
        if (entity == null) {
            return "";
        }
        // TODO: сделай путь!
        return markdownService.toHtmlSoft(entity.getDescription(), "descriptions");
    }


    /**
     * Конвертирует имена изображений и файлов в ссылки.
     */
    private List<String> toFilesLinks(List<String> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .map(e -> PathUtil.normalize(Paths.get(aliasPaths.softUrl(), e).toString(), true))
                .toList();
    }

}

