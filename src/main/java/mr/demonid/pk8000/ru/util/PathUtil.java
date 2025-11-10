package mr.demonid.pk8000.ru.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;


@Log4j2
public class PathUtil {

    /**
     * Приводит путь к единому виду (UNIX-стиль, с ведущим "/").
     * Не зависит от ОС и может использоваться для виртуальных путей в вебе.
     */
    public static String normalize(String path, boolean firstSlash) {
        if (path == null || path.isBlank()) return "";

        // заменяем обратные слеши на прямые
        String result = path.replace("\\", "/")
                .replaceAll("/+", "/")
                .replaceAll("/+$", "");             // убираем хвостовые /

        if (firstSlash) {
            // гарантируем наличие ведущего "/"
            if (!result.startsWith("/"))
                result = "/" + result;
        } else {
            if (result.startsWith("/"))
                result = result.substring(1);
        }
        return result;
    }

    /**
     * Преобразует внутренний (UNIX) путь обратно в системный.
     */
    public static Path toSystemPath(String path) {
        return Paths.get(path.replace("/", File.separator));
    }
    public static Path toSystemPath(Path path) {
        return Paths.get(path.toString().replace("/", File.separator));
    }


    /**
     * Перевод пути в абсолютный.
     * @param root Корень.
     * @param path Относительный путь (если абсолютный, то просто нормализуется).
     */
    public static Path toAbsolutePath(Path root, Path path) {
        Path local = toSystemPath(path.toString()).normalize();
        return local.isAbsolute() ? local : root.resolve(local).normalize();
    }


    /**
     * Извлекает имя файла из пути.
     *
     * @param path Путь (абсолютный или относительный).
     * @return Имя файла, или пустую строку.
     */
    public static String extractFileName(String path) {
        if (path == null || path.isBlank())
            return "";
        Path p = Paths.get(path);
        return p.getFileName().toString();
    }


    /**
     * Извлекает имя файла из пути, без расширения.
     *
     * @param path Путь (абсолютный или относительный).
     * @return Имя файла, или пустую строку.
     */
    public static String extractFileNameWithoutExtension(String path) {
        if (path == null || path.isBlank())
            return "";
        String name = Paths.get(path).getFileName().toString();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex != -1) ? name.substring(0, dotIndex) : name;
    }


    /**
     * Перемещает файл с временной папки в заданный каталог.
     */
    public static void moveTempFileTo(Path src, String destPath, String destFileName) throws IOException {
        Path dest = Paths.get(destPath).toAbsolutePath().normalize();
        Files.createDirectories(dest);
        Path finalFile = dest.resolve(Objects.requireNonNull(destFileName));
        Files.move(src, finalFile, StandardCopyOption.REPLACE_EXISTING);
    }


    /**
     * Загружает файл во временную папку с уникальным именем.
     */
    public static Path loadToTempDirectory(Path tempDir, MultipartFile file) throws Exception {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new IllegalArgumentException("Некорректное имя файла.");
        }

        Path tmpDir = Paths.get(tempDir.toString()).toAbsolutePath().normalize();
        log.info("Load file '{}' to temp directory: '{}'", file.getOriginalFilename(), tmpDir);

        Files.createDirectories(tmpDir);
        Path tmpFile = tmpDir.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
        file.transferTo(tmpFile.toFile());
        return tmpFile;
    }

    /**
     * Удаление каталога со всем содержимым.
     */
    public static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            try (var stream = Files.walk(path)) {
                var sorted = stream.sorted(Comparator.reverseOrder());
                for (Path elem : (Iterable<Path>) sorted::iterator) {
                    Files.delete(elem);
                }
            }
        }
    }


}
