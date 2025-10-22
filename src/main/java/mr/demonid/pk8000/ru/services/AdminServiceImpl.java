package mr.demonid.pk8000.ru.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.CategoryEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.SoftUpdateRequest;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.repository.CategoryRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import mr.demonid.pk8000.ru.util.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;


/**
 * Сервисы по добавлению/изменению данных сайта.
 */
@Service
@AllArgsConstructor
@Log4j2
public class AdminServiceImpl {

    private SoftRepository softRepository;
    private CategoryRepository categoryRepository;
    private AppConfiguration config;
    private SoftMapper softMapper;


    /**
     * Возвращает постраничный список товаров для админки.
     * TODO: доделать фильтр!
     */
    @Transactional(readOnly = true)
    public Page<SoftResponse> getAllProducts(SoftFilter softFilter, Pageable pageable) {
        Page<SoftEntity> items = softRepository.findAll(pageable);
        return items.map(softMapper::toResponse);
    }


    /**
     * Добавляет в БД новый продукт (программу).
     * На этом этапе не учитываются списки файлов и изображений,
     * их можно добавить потом.
     */
    @Transactional
    public void createProduct(SoftRequest request) {
        if (request == null || request.category() == null) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
        }
        try {
            CategoryEntity category = categoryRepository.findById(request.category()).orElse(null);
            if (category == null) {
                throw new ServiceException(ErrorCodes.BAD_SOFT_CATEGORY, "Неверная категория добавляемой программы");
            }
            SoftEntity soft = softMapper.toEntity(request, category);
            softRepository.save(soft);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Обновление информации о продукте.
     * Кроме изображений и архивов, для них отдельные API.
     */
    @Transactional
    public void updateProduct(SoftUpdateRequest request) {
        if (request == null || request.category() == null || request.id() == null) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
        }
        try {
            CategoryEntity category = categoryRepository.findById(request.category()).orElse(null);
            if (category == null) {
                throw new ServiceException(ErrorCodes.BAD_SOFT_CATEGORY, "Неверная категория добавляемой программы");
            }
            SoftEntity soft = softRepository.findById(request.id()).orElse(null);
            if (soft == null) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден");
            }
            soft.setName(request.name());
            soft.setCategory(category);
            soft.setShortDescription(request.shortDescription());
            soft.setDescription(request.description());

            softRepository.save(soft);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Удаление продукта.
     */
    @Transactional
    public void deleteProduct(Long id) {
        try {
            softRepository.deleteById(id);
            deleteDirectory(Paths.get(config.getSoftImagesPath(), id.toString()));
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Обновление существующего, или добавление нового изображения.
     * @param productId       Продукт.
     * @param file            Файл от клиента.
     * @param replaceFileName Имя существующего файла или null.
     */
    public void updateImage(Long productId, MultipartFile file, String replaceFileName) {
        try {
            if (file.isEmpty()) {
                throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
            }
            SoftEntity soft = softRepository.findById(productId).orElse(null);
            if (soft == null) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден");
            }
            // проверяем, существует ли заменяемый файл
            if (replaceFileName != null && !replaceFileName.isEmpty()) {
                if (!soft.getImageFiles().contains(replaceFileName)) {
                    throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Файл '" + replaceFileName + "' не найден");
                }
            }
            // сохраняем во временную папку
            Path tmpFile = loadToTempDirectory(file);
            // переносим в папку изображений
            String finalFileName = replaceFileName == null ? file.getOriginalFilename() : replaceFileName.isBlank() ? file.getOriginalFilename() : replaceFileName;
            moveToImageDirectory(tmpFile, Paths.get(config.getSoftImagesPath(), productId.toString()).toString(), finalFileName);

            // корректируем БД
            if (replaceFileName == null || replaceFileName.isEmpty()) {
                soft.getImageFiles().add(finalFileName);
                softRepository.save(soft);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Удаление изображения.
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла. Удаляет как с БД, так и с диска.
     */
    @Transactional
    public void deleteImage(Long productId, String fileName) {
        try {
            if (productId == null || fileName == null || fileName.isEmpty()) {
                throw new Exception("Некорректные данные");
            }
            SoftEntity soft = softRepository.findById(productId).orElse(null);
            if (soft == null) {
                throw new Exception("Товар не найден");
            }
            if (!soft.getImageFiles().contains(fileName)) {
                throw new Exception("Товар не содержит такого изображения");
            }
            // удаляем из БД
            soft.getImageFiles().remove(fileName);
            softRepository.save(soft);

            // удаляем файл с носителя
            Path imgFile = Paths.get(config.getSoftImagesPath(), productId.toString(), fileName).toAbsolutePath().normalize();
            Files.deleteIfExists(imgFile);

        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Обновление существующего, или добавление нового архива.
     * @param productId       Продукт.
     * @param file            Файл от клиента.
     * @param replaceFileName Имя существующего файла или null.
     */
    public void updateArchive(Long productId, MultipartFile file, String replaceFileName) {
        try {
            if (file.isEmpty()) {
                throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
            }
            SoftEntity soft = softRepository.findById(productId).orElse(null);
            if (soft == null) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден");
            }
            // проверяем, существует ли заменяемый файл
            if (replaceFileName != null && !replaceFileName.isEmpty()) {
                if (!soft.getArchiveFiles().contains(replaceFileName)) {
                    throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Файл '" + replaceFileName + "' не найден");
                }
            }
            // сохраняем во временную папку
            Path tmpFile = loadToTempDirectory(file);
            // переносим в папку изображений
            String finalFileName = replaceFileName == null ? file.getOriginalFilename() : replaceFileName.isBlank() ? file.getOriginalFilename() : replaceFileName;
            moveToImageDirectory(tmpFile, Paths.get(config.getSoftFilesPath(), productId.toString()).toString(), finalFileName);

            // корректируем БД
            if (replaceFileName == null || replaceFileName.isEmpty()) {
                soft.getArchiveFiles().add(finalFileName);
                softRepository.save(soft);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Удаление архива.
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла. Удаляет как с БД, так и с диска.
     */
    @Transactional
    public void deleteArchive(Long productId, String fileName) {
        try {
            if (productId == null || fileName == null || fileName.isEmpty()) {
                throw new Exception("Некорректные данные");
            }
            SoftEntity soft = softRepository.findById(productId).orElse(null);
            if (soft == null) {
                throw new Exception("Товар не найден");
            }
            if (!soft.getArchiveFiles().contains(fileName)) {
                throw new Exception("Товар не содержит такого изображения");
            }
            // удаляем из БД
            soft.getArchiveFiles().remove(fileName);
            softRepository.save(soft);

            // удаляем файл с носителя
            Path imgFile = Paths.get(config.getSoftFilesPath(), productId.toString(), fileName).toAbsolutePath().normalize();
            Files.deleteIfExists(imgFile);

        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


//    public void deleteFile(Long id, String fileName, Supplier<List<String>> action, String path) {
//        try {
//            if (id == null || fileName == null || fileName.isEmpty()) {
//                throw new Exception("Некорректные данные");
//            }
//            SoftEntity soft = softRepository.findById(id).orElse(null);
//            if (soft == null) {
//                throw new Exception("Товар не найден");
//            }
//            if (!action.get().contains(fileName)) {
//                throw new Exception("Товар не содержит такого изображения");
//            }
//            // удаляем из БД
//            action.get().remove(fileName);
//            softRepository.save(soft);
//
//            // удаляем файл с носителя
//            Path imgFile = Paths.get(path, id.toString(), fileName).toAbsolutePath().normalize();
//            Files.deleteIfExists(imgFile);
//
//        } catch (Exception e) {
//            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
//        }
//    }


    /*
     * Перемещает файл с временной папки в каталог изображений
     */
    private void moveToImageDirectory(Path src, String destPath, String destFileName) {
        try {
            Path picsDir = Paths.get(destPath).toAbsolutePath().normalize();
            Files.createDirectories(picsDir);
            Path finalFile = picsDir.resolve(Objects.requireNonNull(destFileName));
            Files.move(src, finalFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    /*
     * Сохраняет пришедший файл во временную папку
     */
    private Path loadToTempDirectory(MultipartFile file) {
        // сохраняем во временную папку
        try {
            Path tmpDir = Paths.get(config.getTempDirectory()).toAbsolutePath().normalize();
            log.info("LoadToTemp(): '{}'", tmpDir);

            Files.createDirectories(tmpDir);
            Path tmpFile = tmpDir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
            file.transferTo(tmpFile.toFile());
            // проверяем тип файла
            if (!FileType.isImage(tmpFile) && !FileType.isArchive(tmpFile)) {
                // удаляем и возвращаем ошибку
                Files.deleteIfExists(tmpFile);
                throw new Exception("Файл '" + tmpFile.toFile() + "' не является изображением");
            }
            return tmpFile;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    /*
     * Удаление каталога со всем содержимым.
     */
    private void deleteDirectory(Path path) {
        try (Stream<Path> stream = Files.walk(path)) {
            stream.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            log.error("Не удалось удалить '{}': {}", p, e);
                        }
                    });
        } catch (IOException e) {
            log.error("Ошибка удаления: {}", e.getMessage());
        }
    }

}
