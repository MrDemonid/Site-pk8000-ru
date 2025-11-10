package mr.demonid.pk8000.ru.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.DescriptionMetaRequest;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.util.PathTool;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;


@Service
@Log4j2
@RequiredArgsConstructor
public class AtomicFileOperationService {

    private final PathTool pathTool;


    /**
     * Атомарно обновляет каталог с описанием ресурса.
     *
     * @param baseDir     Каталог ресурса, например /content/soft/descriptions/tetris/
     * @param attachments Новые файлы
     * @param meta        Метаданные с информацией о заменах и удалениях
     */
    public void performAtomicUpdate(Path baseDir,
                                    List<MultipartFile> attachments,
                                    DescriptionMetaRequest meta) {

        long uuid = Instant.now().toEpochMilli();   // для удобства идентификатор будет в миллисекундах, а не в UUID.
        Path tempRoot = pathTool.getTempPath();
        Path tempWork = null;
        Path backupDir = null;

        try {
            // Считаем хеш текущего каталога.
            byte[] oldHash = hashDirectory(baseDir);
            log.debug("Old hash for {}: {}", baseDir, hashToString(oldHash));

            Files.createDirectories(tempRoot);
            Files.createDirectories(baseDir);

            // Создаём временную копию ресурса
            tempWork = Files.createTempDirectory(tempRoot, "work_" + uuid);
            FileUtils.copyDirectory(baseDir.toFile(), tempWork.toFile());
            log.debug("A temporary copy has been created: {}", tempWork);

            // Применяем операции к копии
            applyChanges(tempWork, attachments, meta);

            // Создаём бэкап для возможного rollback
            backupDir = Files.createTempDirectory(tempRoot, "backup_" + uuid);
            FileUtils.copyDirectory(baseDir.toFile(), backupDir.toFile());
            log.debug("A backup of the original has been created: {}", backupDir);

            // Проверяем хеш перед заменой.
            byte[] currentHash = hashDirectory(baseDir);
            if (!Arrays.equals(oldHash, currentHash)) {
                throw new ServiceException(ErrorCodes.CONCURRENT_MODIFICATION, "Resource has been modified by another user, please retry");
            }
            // Заменяем оригинал
            safelyReplace(baseDir, tempWork);

            // Удаляем backup после успешного завершения
            FileUtils.deleteDirectory(backupDir.toFile());
            log.info("Atomic update completed successfully: {}", baseDir);

        } catch (ServiceException se) {
            log.warn(se.getMessage());
            cleanupTemp(tempWork);
            throw se;
        } catch (Exception e) {
            log.error("Error during atomic update {}", baseDir, e);
            rollback(baseDir, backupDir);
            throw new ServiceException(ErrorCodes.IO_ERROR, "Update error: " + e.getMessage());
        } finally {
            cleanupTemp(tempWork);
        }
    }


    /**
     * Производит изменения во временной папке, в соответствии с attachments и метаданными.
     *
     * @param tempWork    Временный каталог, со структурой оригинала.
     * @param attachments Добавляемые файлы.
     * @param meta        Метаданные (удаление/замена файлов).
     */
    private void applyChanges(Path tempWork,
                              List<MultipartFile> attachments,
                              DescriptionMetaRequest meta) throws IOException {

        // Удаляем файлы.
        if (meta.removed() != null) {
            for (String rel : meta.removed()) {
                Path target = validateAndResolve(tempWork, rel);
                Files.deleteIfExists(target);
                log.info("File deleted: {}", rel);
            }
        }

        // Заменяем исходные файлы на новые.
        if (meta.replaced() != null) {
            for (var entry : meta.replaced().entrySet()) {
                String oldName = entry.getKey();
                String newName = entry.getValue();

                MultipartFile newFile = findAndRemoveAttachment(attachments, newName);
                if (newFile == null) {
                    log.warn("Replacement file not found: {}", newName);
                    continue;
                }

                Path target = validateAndResolve(tempWork, oldName);
                Files.deleteIfExists(target);
                Files.createDirectories(target.getParent());
                newFile.transferTo(target);
                log.info("File replaced {} → {}", oldName, newName);
            }
        }

        // Добавляем новые.
        if (attachments != null) {
            for (MultipartFile f : attachments) {
                String name = f.getOriginalFilename();
                if (name == null || name.isBlank())
                    continue;
                Path target = validateAndResolve(tempWork, name);
                if (!Files.exists(target)) {
                    Files.createDirectories(target.getParent());
                    f.transferTo(target);
                    log.info("New file added: {}", name);
                }
            }
        }
    }

    private MultipartFile findAndRemoveAttachment(List<MultipartFile> attachments, String name) {
        if (attachments == null) return null;
        Iterator<MultipartFile> it = attachments.iterator();
        while (it.hasNext()) {
            MultipartFile f = it.next();
            if (name.equals(f.getOriginalFilename())) {
                it.remove(); // удаляем из списка, чтобы не обрабатывать потом
                return f;
            }
        }
        return null;
    }

    private MultipartFile findAttachment(List<MultipartFile> attachments, String name) {
        if (attachments == null) return null;
        return attachments.stream().filter(f -> name.equals(f.getOriginalFilename())).findFirst().orElse(null);
    }


    /**
     * Замена каталога.
     * @param baseDir  Исходный каталог.
     * @param tempWork Каталог с новыми данными.
     */
    private void safelyReplace(Path baseDir, Path tempWork) throws IOException {
        Path parent = baseDir.getParent();
        if (parent == null)
            throw new IOException("Incorrect path: " + baseDir);

        Path tempFinal = parent.resolve(baseDir.getFileName().toString() + "_new");

        // Перемещаем tempWork в соседний временный каталог
        Files.move(tempWork, tempFinal, StandardCopyOption.ATOMIC_MOVE);

        // Удаляем старый каталог и заменяем
        FileUtils.deleteDirectory(baseDir.toFile());
        Files.move(tempFinal, baseDir, StandardCopyOption.ATOMIC_MOVE);

        log.debug("Directory {} successfully replaced", baseDir);
    }


    /**
     * Откат. Восстанавливаем исходный каталог из бэкапа.
     * @param baseDir   Исходный каталог.
     * @param backupDir Каталог бэкапа.
     */
    private void rollback(Path baseDir, Path backupDir) {
        if (backupDir == null || !Files.exists(backupDir)) {
            log.warn("Rollback is not possible - there is no backup");
            return;
        }
        try {
            FileUtils.deleteDirectory(baseDir.toFile());
            FileUtils.copyDirectory(backupDir.toFile(), baseDir.toFile());
            log.warn("Changes rolled back for {}", baseDir);
        } catch (IOException e) {
            log.error("Error rollback: {}", e.getMessage());
        }
    }


    /**
     * Удаление времянки.
     */
    private void cleanupTemp(Path dir) {
        if (dir == null) return;
        try {
            FileUtils.deleteDirectory(dir.toFile());
        } catch (IOException ignored) {
        }
    }

    /**
     * Валидация и безопасное разрешение относительного пути.
     * Предотвращает directory traversal (../ и абсолютные пути).
     * То есть, не даем выйти за пределы каталога ресурса.
     */
    private Path validateAndResolve(Path base, String relative) throws IOException {
        if (relative == null || relative.isBlank())
            throw new IOException("Empty file name");
        if (relative.contains("..") || relative.contains(":") || relative.startsWith("/"))
            throw new IOException("Invalid path: " + relative);

        Path resolved = base.resolve(relative).normalize();
        if (!resolved.startsWith(base))
            throw new IOException("Attempt to exit directory boundaries: " + relative);

        return resolved;
    }


    /**
     * Высчитывает хэш каталога.
     */
    private byte[] hashDirectory(Path dir) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (Stream<Path> stream = Files.walk(dir)) {
            stream.filter(Files::isRegularFile)
                    .sorted() // важно, чтобы порядок был стабильным
                    .forEach(p -> {
                        try {
                            digest.update(Files.readAllBytes(p));
                            digest.update(p.toString().getBytes(StandardCharsets.UTF_8));
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }

        return digest.digest();
    }

    private String hashToString(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }

}
