package mr.demonid.pk8000.ru.services.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AliasPaths;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.CategoryEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.SoftCreateRequest;
import mr.demonid.pk8000.ru.dto.SoftUpdateRequest;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.repository.CategoryRepository;
import mr.demonid.pk8000.ru.repository.ProductArchivesRepository;
import mr.demonid.pk8000.ru.repository.ProductImagesRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Сервисы по добавлению/изменению данных сайта.
 */
@Service
@AllArgsConstructor
@Log4j2
public class AdminServiceImpl {

    private SoftRepository softRepository;
    private ProductImagesRepository imagesRepository;
    private ProductArchivesRepository archivesRepository;

    private CategoryRepository categoryRepository;
    private AppConfiguration config;
    private AliasPaths aliasPaths;
    private SoftMapper softMapper;


    /**
     * Добавляет в БД новый продукт (программу).
     * На этом этапе не учитываются списки файлов и изображений,
     * их можно добавить потом.
     */
    @Transactional
    public void createProduct(SoftCreateRequest request) {
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

            softRepository.save(soft);

            log.info("Update product [ id = {}, name = '{}', short desc = '{}', category = '{}']", request.id(), request.name(), request.shortDescription(), category.getName());

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
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


    /**
     * Обновление существующего, или добавление нового архива.
     *
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
//            // сохраняем во временную папку
//            Path tmpFile = loadToTempDirectory(file);
//            // переносим в папку изображений
//            String finalFileName = replaceFileName == null ? file.getOriginalFilename() : replaceFileName.isBlank() ? file.getOriginalFilename() : replaceFileName;
//            moveToImageDirectory(tmpFile, Paths.get(aliasPaths.softPath(), productId.toString()).toString(), finalFileName);

            // корректируем БД
            if (replaceFileName == null || replaceFileName.isEmpty()) {
                // TODO: !!!!!
//                soft.getArchiveFiles().add(finalFileName);
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
     *
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
            Path imgFile = Paths.get(aliasPaths.softPath(), productId.toString(), fileName).toAbsolutePath().normalize();
            Files.deleteIfExists(imgFile);

        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }



//    /*
//     * Удаление каталога со всем содержимым.
//     */
//    private void deleteDirectory(Path path) {
//        try (Stream<Path> stream = Files.walk(path)) {
//            stream.sorted(Comparator.reverseOrder())
//                    .forEach(p -> {
//                        try {
//                            Files.delete(p);
//                        } catch (IOException e) {
//                            log.error("Не удалось удалить '{}': {}", p, e);
//                        }
//                    });
//        } catch (IOException e) {
//            log.error("Ошибка удаления: {}", e.getMessage());
//        }
//    }

}
