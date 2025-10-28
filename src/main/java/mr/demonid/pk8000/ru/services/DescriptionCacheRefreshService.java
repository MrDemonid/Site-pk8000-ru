package mr.demonid.pk8000.ru.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.SoftDescriptionFileEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.repository.SoftDescriptionFileRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;


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
    private final SoftRepository softRepository;
    private final AppConfiguration config;


    @Transactional
    public void refreshAll() {
        log.info("Refreshing description cache...");
        long startTime = System.currentTimeMillis();
        List<SoftDescriptionFileEntity> files = fileRepository.findAll();

        for (SoftDescriptionFileEntity meta : files) {
            switch (isDescriptionCacheOutdated(meta)) {
                case FILE_NOT_FOUND:
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
    private void deleteCache(SoftDescriptionFileEntity meta) {
        log.info("File {} not found. Delete cache.", Path.of(config.getDescDirectory(), meta.getFilePath()));
        SoftEntity product = meta.getProduct();
        product.setDescription("");
        softRepository.save(product);

        // обновляем метаданные, чтобы гарантированно не совпали с добавляемым впоследствии файлом.
        meta.setFileModifiedAt(0L);
        meta.setFileCreatedAt(0L);
        meta.setFileSize(0L);
        fileRepository.save(meta);
    }

    /**
     * Обновление/добавление описания файла в кеш продукта.
     */
    private void updateCache(SoftDescriptionFileEntity meta) {
        log.info("File changed, updating cache for product {}", meta.getProduct().getName());
        Path path = Path.of(config.getDescDirectory(), meta.getFilePath());
        try {
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            String content = Files.readString(path);

            // обновляем кеш в основном продукте
            SoftEntity product = meta.getProduct();
            product.setDescription(content);
            softRepository.save(product);

            // обновляем метаданные
            meta.setFileModifiedAt(attrs.lastModifiedTime().toMillis());
            meta.setFileCreatedAt(attrs.creationTime().toMillis());
            meta.setFileSize(attrs.size());
            fileRepository.save(meta);
        } catch (Exception e) {
            log.error("Cannot update cache for product {}: {}", meta.getProduct().getName(), e.getMessage());
        }

    }

    /**
     * Проверка валидности кеша продукта, по атрибутам соответствующего файла.
     */
    private CacheStatus isDescriptionCacheOutdated(SoftDescriptionFileEntity meta) {
        try {
            Path path = Path.of(config.getDescDirectory(), meta.getFilePath());
            if (!Files.exists(path)) {
                log.warn("isDescriptionCacheOutdated(). File not found: {}", meta.getFilePath());
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
            log.error("isDescriptionCacheOutdated(). IO error: ", e);
            return CacheStatus.IO_ERROR;
        }
    }


    private void showTime(long time) {
        log.info("Cache refresh complete at {} milliseconds", time);
    }

}
