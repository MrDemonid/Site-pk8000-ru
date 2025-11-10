package mr.demonid.pk8000.ru.services.admin;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.SoftDescriptionFileEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.DescriptionMetaRequest;
import mr.demonid.pk8000.ru.dto.DescriptionResponse;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.repository.SoftDescriptionFileRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.AtomicFileOperationService;
import mr.demonid.pk8000.ru.services.DescriptionCacheRefreshService;
import mr.demonid.pk8000.ru.services.ZipService;
import mr.demonid.pk8000.ru.util.PathTool;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Log4j2
@Transactional
public class DescriptionService {

    private final SoftDescriptionFileRepository repository;
    private final SoftRepository softRepository;
    private final PathTool pathTool;
    private final ZipService zipService;
    private final AtomicFileOperationService atomicFileOperationService;
    private final DescriptionCacheRefreshService refreshService;


    /**
     * Возвращает состав описателя продукта.
     *
     * @param productId Продукт.
     * @return Имена файлов, входящих в описание продукта.
     */
    public DescriptionResponse getDescription(Long productId) {
        SoftDescriptionFileEntity entity = repository.findByProduct_Id(productId).orElse(null);
        if (entity == null) {
            return new DescriptionResponse(0L, new ArrayList<>());
        }
        return new DescriptionResponse(
                entity.getId(),
                getLinksFiles(productId)
        );
    }


    /**
     * Архивирует описание продукта и возвращает архив в виде массива байт.
     *
     * @param productId Продукт.
     */
    public byte[] archiveProductDescription(Long productId) {
        try {
            if (!repository.existsById(productId)) {
                throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Описание не найдено");
            }

            Path root = getAttachDirectory(productId);
            return zipService.makeZipToBytes(root);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Обновление данных описания продукта.
     *
     * @param productId Продукт.
     * @param files     Добавляемые файлы.
     * @param meta      Метаданные (заменяемые/удаляемые файлы)
     */
    public DescriptionResponse updateDescription(Long productId, List<MultipartFile> files, DescriptionMetaRequest meta) {
        SoftDescriptionFileEntity entity = repository.findByProduct_Id(productId).orElse(null);
        if (entity == null) {
            return createDescription(productId, files);
        }

        Path root = getAttachDirectory(productId);
        atomicFileOperationService.performAtomicUpdate(root, files, meta);

        // Подчищаем возможно оставшиеся пустые каталоги и обновляем кеш
        pathTool.cleanDirectory(root);
        refreshService.updateCache(entity);

        return new DescriptionResponse(
                entity.getId(),
                getLinksFiles(productId)
        );
    }


    /**
     * Полное удаление описания.
     *
     * @param productId Продукт.
     */
    public void deleteDescription(Long productId) {
        SoftDescriptionFileEntity entity = repository.findByProduct_Id(productId)
                .orElseThrow(() -> new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Описание не найдено"));

        Path root = getAttachDirectory(productId);
        if (!Files.exists(root)) {
            throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Directory '" + root + "' not found");
        }
        try {
            // удаляем каталог с вложениями и запись в БД
            PathUtil.deleteDirectory(root);
            repository.deleteById(entity.getId());
            log.warn("Product '{}' deleted successfully", productId);

        } catch (IOException e) {
            throw new ServiceException(ErrorCodes.IO_ERROR, e.getMessage());
        }
    }


    /**
     * Создает новую запись описателя.
     *
     * @param productId Продукт.
     * @param files     Список добавляемых файлов. Один обязательно должен быть markdown.
     */
    private DescriptionResponse createDescription(Long productId, List<MultipartFile> files) {
        SoftEntity soft = softRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Product not found"));

        if (files == null || files.isEmpty()) {
            throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Markdown file not found");
        }

        MultipartFile mdFile = files.stream()
                .filter(e -> e.getOriginalFilename() != null && e.getOriginalFilename().endsWith(".md"))
                .findFirst()
                .orElseThrow(() -> new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Markdown file not found"));

        Path root = getAttachDirectory(productId);

        try {
            for (var file : files) {
                if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank())
                    continue;
                Path target = root.resolve(file.getOriginalFilename()).normalize();
                Files.createDirectories(target.getParent());
                file.transferTo(target);
                log.info("New file transferred to {}", target);
            }

            SoftDescriptionFileEntity entity = new SoftDescriptionFileEntity();
            entity.setProduct(soft);
            entity.setFileName(mdFile.getOriginalFilename());
            entity = repository.save(entity);

            // Подчищаем возможно оставшиеся пустые каталоги и обновляем кеш
            pathTool.cleanDirectory(root);
            refreshService.updateCache(entity);

            return new DescriptionResponse(entity.getId(), getLinksFiles(productId));

        } catch (IOException e) {
            throw new ServiceException(ErrorCodes.IO_ERROR, e.getMessage());
        }
    }



    /**
     * Составляет список файлов из каталога описания, включая подкаталоги.
     *
     * @param productId Продукт.
     */
    private List<String> getLinksFiles(Long productId) {
        Path path = getAttachDirectory(productId);
        try (var paths = Files.walk(path)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path::relativize)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (NoSuchFileException ex) {
            log.info("Directory '{}' not found", path);
            return new ArrayList<>();
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }

    }


    /**
     * Возвращает абсолютный путь до папки описателя.
     *
     * @param productId Продукт.
     */
    private Path getAttachDirectory(Long productId) {
        return pathTool.getSoftDescSubdirPath()
                .resolve(productId.toString())
                .normalize();
    }



}
