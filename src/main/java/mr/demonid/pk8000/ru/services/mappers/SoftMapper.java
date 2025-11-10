package mr.demonid.pk8000.ru.services.mappers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.*;
import mr.demonid.pk8000.ru.dto.ArchiveResponse;
import mr.demonid.pk8000.ru.dto.ImageResponse;
import mr.demonid.pk8000.ru.dto.SoftCreateRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.services.markdown.MarkdownService;
import mr.demonid.pk8000.ru.util.AliasPaths;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                toImageLinks(entity.getImageFiles()),
                toFileLinks(entity.getArchiveFiles())
        );
    }

    /**
     * Создает новую сущность.
     */
    public SoftEntity toEntity(SoftCreateRequest response, CategoryEntity categoryEntity) {
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
        return markdownService.toHtmlSoft(entity.getDescription(),
                Path.of(aliasPaths.softDescSubdir(), entity.getProduct().getId().toString()).toString());
    }


    public List<ImageResponse> toImageResponse(SoftEntity entity) {
        return entity.getImageFiles().stream()
                .filter(Objects::nonNull)
                .filter(e -> !e.getFileName().isBlank())
                .map(e -> new ImageResponse(
                        entity.getId(),
                        PathUtil.normalize(Paths.get(aliasPaths.softUrl(), aliasPaths.softImagesSubdir(), e.getFileName()) + "?v=" + e.getVersion(), true),
                        PathUtil.extractFileName(e.getFileName())
                ))
                .toList();
    }

    public List<ArchiveResponse> toArchiveResponse(SoftEntity entity) {
        return entity.getArchiveFiles().stream()
                .filter(Objects::nonNull)
                .filter(e -> !e.getFileName().isBlank())
                .map(e -> new ArchiveResponse(
                        entity.getId(),
                        PathUtil.normalize(Paths.get(aliasPaths.softUrl(), aliasPaths.softFilesSubdir(), e.getFileName()) + "?v=" + e.getVersion(), true),
                        PathUtil.extractFileName(e.getFileName())
                ))
                .toList();
    }

    /**
     * Конвертирует имена изображений в ссылки.
     */
    private List<String> toImageLinks(List<ImagesEntity> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(e -> !e.getFileName().isBlank())
                .map(e -> PathUtil.normalize(
                        Paths.get(aliasPaths.softUrl(),
                                aliasPaths.softImagesSubdir(),
                                e.getFileName()) + "?v=" + e.getVersion(), true)
                )
                .toList();
    }

    /**
     * Конвертирует имена файлов в ссылки.
     */
    private List<String> toFileLinks(List<ArchivesEntity> names) {
        return names.stream()
                .filter(Objects::nonNull)
                .filter(e -> !e.getFileName().isBlank())
                .map(e -> PathUtil.normalize(
                        Paths.get(aliasPaths.softUrl(),
                                aliasPaths.softFilesSubdir(),
                                e.getFileName()) + "?v=" + e.getVersion(), true)
                )
                .toList();
    }


}

