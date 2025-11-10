package mr.demonid.pk8000.ru.services.admin;

import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.ProductFileBase;
import mr.demonid.pk8000.ru.domain.SoftEntity;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.repository.ProductFileRepository;
import mr.demonid.pk8000.ru.repository.SoftRepository;
import mr.demonid.pk8000.ru.services.mappers.SoftMapper;
import mr.demonid.pk8000.ru.util.PathTool;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Log4j2
public abstract class BaseFileService<
        T extends ProductFileBase,
        R extends JpaRepository<T, Long> & ProductFileRepository<T>,
        D> {

    protected final SoftRepository softRepository;
    protected final R repository;
    protected final AppConfiguration config;
    protected final PathTool pathTool;
    protected final SoftMapper softMapper;


    protected BaseFileService(SoftRepository softRepository,
                              R repository,
                              AppConfiguration config,
                              PathTool pathTool,
                              SoftMapper softMapper) {
        this.softRepository = softRepository;
        this.repository = repository;
        this.config = config;
        this.pathTool = pathTool;
        this.softMapper = softMapper;
    }


    /**
     * Проверяет тип файла (изображение, архив и т.д.)
     */
    protected abstract boolean isValidFileType(Path file);

    /**
     * Возвращает имя подкаталога, куда сохраняются файлы.
     */
    protected abstract String getSubdirectory();

    /**
     * Возвращает имя типа файла (для логов и ошибок)
     */
    protected abstract String getFileTypeName();

    /**
     * Маппинг данных ответа.
     */
//    protected abstract List<?> mapResponse(SoftEntity soft);
    protected abstract List<D> mapResponse(SoftEntity soft);

    /**
     * Создание новой сущности конкретного типа.
     */
    protected abstract T createEntity();


    /**
     * Возвращает список файлов продукта.
     */
    public List<D> getFiles(Long productId) {
        SoftEntity soft = softRepository.findById(productId)
                .orElseThrow(() -> new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден"));
        return mapResponse(soft);
    }


    /**
     * Обновление существующего, или добавление нового файла.
     *
     * @param productId       Продукт.
     * @param file            Файл от клиента.
     * @param replaceFileName Имя существующего файла или null.
     */
    public void updateFile(Long productId, MultipartFile file, String replaceFileName) {
        try {
            if (file.isEmpty() || file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
                throw new ServiceException(ErrorCodes.BAD_DATA, "Некорректное имя принимаемого файла");
            }
            SoftEntity product = softRepository.findById(productId)
                    .orElseThrow(() -> new ServiceException(ErrorCodes.SOFT_NOT_FOUND, "Продукт не найден"));

            // получаем список связанных с продуктом файлов
            List<T> files = repository.findByProductId(productId).orElse(new ArrayList<>());
            T entity;

            // решаем: менять существующую запись, или создавать новую
            if (replaceFileName != null && !replaceFileName.isBlank()) {
                entity = files.stream()
                        .filter(e -> replaceFileName.equals(e.getFileName()))
                        .findFirst()
                        .orElseThrow(() ->
                                new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Файл '" + replaceFileName + "' не найден"));
                entity.incrementVersion();
            } else {
                entity = createEntity();
                entity.setVersion(1);
                entity.setProduct(product);
            }
            entity.setFileName(file.getOriginalFilename());

            // сохраняем во временную папку
            Path tmpFile = PathUtil.loadToTempDirectory(getAbsoluteTempDirectory(), file);

            // валидация
            if (!isValidFileType(tmpFile)) {
                Files.deleteIfExists(tmpFile);
                throw new ServiceException(ErrorCodes.BAD_DATA, "Файл '" + file.getOriginalFilename() + "' не является допустимым типом: " + getFileTypeName());
            }

            // перенос в конечную папку
            String targetDir = getAbsoluteSubdirectory().toString();
            PathUtil.moveTempFileTo(tmpFile, targetDir, entity.getFileName());

            // сохраняем запись
            repository.save(entity);

            // удаляем старый файл
            if (replaceFileName != null && !replaceFileName.isBlank() && !replaceFileName.equals(entity.getFileName())) {
                Path path = Paths.get(targetDir, replaceFileName).toAbsolutePath().normalize();
                Files.deleteIfExists(path);
                log.info("Delete older file: {}", path);
            }
            if (replaceFileName != null && !replaceFileName.isBlank()) {
                log.info("Replace file [{}] to [{}]", replaceFileName, entity.getFileName());
            } else {
                log.info("Add file [{}]", entity.getFileName());
            }

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    /**
     * Удаление файла.
     *
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла. Удаляет как с БД, так и с диска.
     */
    public void deleteFile(Long productId, String fileName) {
        try {
            if (productId == null || fileName == null || fileName.isBlank()) {
                throw new ServiceException(ErrorCodes.BAD_DATA, "Некорректные данные");
            }
            List<T> files = repository.findByProductId(productId).orElse(new ArrayList<>());
            T entity = files.stream()
                    .filter(f -> fileName.equals(f.getFileName()))
                    .findFirst()
                    .orElseThrow(() ->
                            new ServiceException(ErrorCodes.FILE_NOT_FOUND, "Файл '" + fileName + "' не найден"));
            // удаляем из БД
            repository.deleteById(entity.getId());

            Path targetPath = getAbsoluteSubdirectory().resolve(fileName);
            Files.deleteIfExists(targetPath);
            log.info("Delete file: [{}]", fileName);

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.UNKNOWN_ERROR_CODE, e.getMessage());
        }
    }

    public void removePhysicFiles(Long productId) {
        if (productId == null) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Некорректные данные");
        }
        List<T> files = repository.findByProductId(productId).orElse(new ArrayList<>());
        files.forEach(f -> {
            Path targetPath = getAbsoluteSubdirectory().resolve(f.getFileName());
            try {
                Files.deleteIfExists(targetPath);
                log.info("Remove file: [{}]", targetPath);
            } catch (IOException e) {
                log.error("Can't remove file: {}", e.getMessage());
            }
        });
    }

    private Path getAbsoluteSubdirectory() {
        return pathTool.getSoftPath().resolve(getSubdirectory()).normalize();
    }

    private Path getAbsoluteTempDirectory() {
        return pathTool.getTempPath();
    }
}
