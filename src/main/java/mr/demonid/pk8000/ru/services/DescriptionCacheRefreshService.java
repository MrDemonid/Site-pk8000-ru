package mr.demonid.pk8000.ru.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.SoftDescriptionFileEntity;
import mr.demonid.pk8000.ru.repository.SoftDescriptionFileRepository;
import mr.demonid.pk8000.ru.util.PathTool;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class DescriptionCacheRefreshService {

    enum CacheStatus {
        FILE_NOT_FOUND,     // файл не найден
        IO_ERROR,           // ошибка обращения к файлу
        NO_CHANGES,         // файл и кэш совпадают
        CHANGES             // файл отличается от кеша
    }

    private final SoftDescriptionFileRepository fileRepository;
    private final PathTool pathTool;


    @Transactional
    public void refreshAll() {
        log.info("Refreshing description cache...");
        long startTime = System.currentTimeMillis();
        List<SoftDescriptionFileEntity> files = fileRepository.findAll();

        for (SoftDescriptionFileEntity meta : files) {
            switch (isDescriptionCacheOutdated(meta)) {
                case FILE_NOT_FOUND:
                    log.info("File {} not found. Delete cache.", getPath(meta));
                    deleteCache(meta);
                    break;
                case CHANGES:
                    updateCache(meta);
                    break;
            }
        }
        showTime(System.currentTimeMillis() - startTime);
    }


    /**
     * Удаление описания файла из кеша продукта.
     */
    public void deleteCache(SoftDescriptionFileEntity meta) {
        if (meta != null && meta.getId() != null) {
            log.info("Deleting cache for {}", meta);
            fileRepository.deleteById(meta.getId());
            try {
                Path baseDir = getPath(meta).getParent();
                FileUtils.deleteDirectory(baseDir.toFile());
            } catch (Exception e) {
                log.warn("Error deleting cache file '{}': {}", meta, e.getMessage());
            }
        }
    }

    public void deleteCache(Long productId) {
        deleteCache(fileRepository.findByProduct_Id(productId).orElse(null));
    }


    /**
     * Обновление/добавление описания файла в кеш продукта.
     */
    public void updateCache(SoftDescriptionFileEntity meta) {
        log.info("File changed, updating cache for product '{}'", meta.getProduct().getName());
        Path path = getPath(meta);
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            String content = Files.readString(path);
            meta.setDescription(content);
            // обновляем метаданные
            meta.setFileModifiedAt(attrs.lastModifiedTime().toMillis());
            meta.setFileCreatedAt(attrs.creationTime().toMillis());
            meta.setFileSize(attrs.size());
            fileRepository.save(meta);
        } catch (Exception e) {
            log.error("Cannot update cache for product '{}': {}", meta.getProduct().getName(), e.getMessage());
        }

    }

    /**
     * Проверка валидности кеша продукта, по атрибутам соответствующего файла.
     */
    private CacheStatus isDescriptionCacheOutdated(SoftDescriptionFileEntity meta) {
        try {
            Path path = getPath(meta);
            if (!Files.exists(path)) {
                log.warn("isDescriptionCacheOutdated(). File not found: {}", path);
                return CacheStatus.FILE_NOT_FOUND;
            }
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

            if (meta.getFileCreatedAt() != attrs.creationTime().toMillis()
                    || meta.getFileModifiedAt() != attrs.lastModifiedTime().toMillis()
                    || meta.getFileSize() != attrs.size()
            )
                return CacheStatus.CHANGES;

            return CacheStatus.NO_CHANGES;

        } catch (Exception e) {
            // считаем, что кеш устарел
            log.error("isDescriptionCacheOutdated(). IO error: {}", e.getMessage());
            return CacheStatus.IO_ERROR;
        }
    }


    private void showTime(long time) {
        log.info("Cache refresh complete at {} milliseconds", time);
    }

    private Path getPath(SoftDescriptionFileEntity meta) {
        return pathTool.getSoftDescSubdirPath()
                .resolve(meta.getProduct().getId().toString())
                .resolve(meta.getFileName());
    }


    /**
     * Очищает HTML от потенциально опасного кода.
     * @param html Исходный HTML.
    //            String clean = cleanHtml(html);
     */
    public String cleanHtml(String html) {
        Safelist safe = Safelist.relaxed().addAttributes("span", "style");
        Document doc = Jsoup.parseBodyFragment(html);
        for (Element element : doc.select("[style]")) {
            String style = element.attr("style");
            // удаляем любой стиль, кроме цветовых
            String cleaned = Arrays.stream(style.split(";"))
                    .map(String::trim)
                    .filter(s -> s.matches("(?i)^(color|background-color)\\s*:\\s*#?[a-z0-9()%,\\s]+$"))
                    .collect(Collectors.joining("; "));
            element.attr("style", cleaned);
        }
        return Jsoup.clean(doc.body().html(), safe);
    }

}
