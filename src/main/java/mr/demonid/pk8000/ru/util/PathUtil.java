package mr.demonid.pk8000.ru.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Log4j2
public class PathUtil {

    private static Path root;

    PathUtil() {
        findRootPath();
        log.info("-- Root Path: {}", root);
    }

    public static Path getRootPath() {
        if (root == null) {
            return Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        }
        return root;
    }

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


    /**
     * Извлекает имя файла из пути.
     * @param path Путь (абсолютный или относительный).
     * @return Имя файла, или пустую строку.
     */
    public static String extractFileName(String path) {
        if (path == null || path.isBlank()) return "";
        Path p = Paths.get(path);
        return p.getFileName().toString();
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

//        // проверяем тип файла
//        if (!FileType.isImage(tmpFile) && !FileType.isArchive(tmpFile)) {
//            // удаляем и возвращаем ошибку
//            Files.deleteIfExists(tmpFile);
//            throw new Exception("Файл '" + tmpFile.toFile() + "' не является изображением");
//        }
//        return tmpFile;
    }



    /*
    Определяем путь к папке проекта/jar-файла
     */
    private void findRootPath() {
        ApplicationHome home = new ApplicationHome(PathUtil.class);
        File source = home.getSource();
        if (source != null && source.isFile() && source.getName().endsWith(".jar")) {
            root = Paths.get(home.getDir().getAbsolutePath());
        } else {
            root = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        }
    }


}
