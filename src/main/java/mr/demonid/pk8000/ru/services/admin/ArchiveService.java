package mr.demonid.pk8000.ru.services.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AliasPaths;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.ArchivesEntity;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.dto.ArchiveResponse;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.repository.ProductArchivesRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import mr.demonid.pk8000.ru.util.FileType;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Сервис управления файлами динамических страниц.
 */
@Service
@AllArgsConstructor
@Log4j2
public class ArchiveService {

    private SoftRepository softRepository;
    private ProductArchivesRepository archivesRepository;

    private AppConfiguration config;
    private AliasPaths aliasPaths;
    private SoftMapper softMapper;


    /**
     * Возвращает список файлов продукта.
     */
    public List<ArchiveResponse> getArchives(Long productId) {
        try {
            SoftEntity soft = softRepository.findById(productId).orElse(null);
            if (soft == null) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден");
            }
            return softMapper.toArchiveResponse(soft);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    /**
     * Обновление существующего, или добавление нового файла.
     *
     * @param productId       Продукт.
     * @param file            Файл от клиента.
     * @param replaceFileName Имя существующего файла или null.
     */
    @Transactional
    public void updateArchive(Long productId, MultipartFile file, String replaceFileName) {
        try {
            if (file.isEmpty() || Objects.requireNonNull(file.getOriginalFilename()).isBlank()) {
                throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
            }
            if (!softRepository.existsById(productId)) {
                throw new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден");
            }
            // получаем список связанных с продуктом
            List<ArchivesEntity> files = archivesRepository.findByProductId(productId).orElse(new ArrayList<>());
            ArchivesEntity archive;

            // решаем: менять существующую запись, или создавать новую
            if (replaceFileName != null && !replaceFileName.isBlank()) {
                archive = files.stream()
                        .filter(e -> replaceFileName.equals(e.getFileName()))
                        .findFirst().orElse(null);
                if (archive == null) {
                    throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Файл '" + replaceFileName + "' не найден");
                }
                archive.incrementVersion();
            } else {
                archive = new ArchivesEntity();
                archive.setVersion(1L);
                archive.setProduct(softRepository.findById(productId).orElse(null));
            }
            archive.setFileName(file.getOriginalFilename());

            // сохраняем во временную папку
            Path tmpFile = PathUtil.loadToTempDirectory(Paths.get(config.getTempDirectory()).toAbsolutePath().normalize(), file);
            if (!FileType.isArchive(tmpFile)) {
                // удаляем и возвращаем ошибку
                Files.deleteIfExists(tmpFile);
                throw new ServiceException(ErrorCodes.BAD_DATA, "Файл '" + tmpFile.toFile() + "' не является архивом");
            }

            // переносим в папку изображений
            PathUtil.moveTempFileTo(tmpFile, Paths.get(aliasPaths.softPath(), aliasPaths.softFilesSubdir()).toString(), archive.getFileName());

            // корректируем БД
            archivesRepository.save(archive);

            // удаляем старый файл
            if (replaceFileName != null && !replaceFileName.isBlank() && !archive.getFileName().equals(replaceFileName)) {
                Path path = Paths.get(aliasPaths.softPath(), aliasPaths.softFilesSubdir(), replaceFileName).toAbsolutePath().normalize();
                log.info("Delete old file: '{}'", path);
                Files.deleteIfExists(path);
            }

            if (replaceFileName != null && !replaceFileName.isEmpty()) {
                log.info("Replace archive [{}]", archive.getFileName());
            } else {
                log.info("Add new archive [{}]", archive.getFileName());
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
            if (productId == null || fileName == null || fileName.isBlank()) {
                throw new ServiceException(ErrorCodes.BAD_DATA, "Поступили некорректные данные");
            }
            List<ArchivesEntity> files = archivesRepository.findByProductId(productId).orElse(new ArrayList<>());
            ArchivesEntity archive = files.stream()
                    .filter(e -> fileName.equals(e.getFileName()))
                    .findFirst().orElse(null);
            if (archive == null) {
                throw new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Файл '" + fileName + "' не найден");
            }

            // удаляем из БД
            archivesRepository.deleteById(archive.getId());

            // удаляем файл с носителя
            Path imgFile = Paths.get(aliasPaths.softPath(), aliasPaths.softFilesSubdir(), fileName).toAbsolutePath().normalize();
            Files.deleteIfExists(imgFile);
            log.info("Delete file: '{}'", imgFile);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }


}
