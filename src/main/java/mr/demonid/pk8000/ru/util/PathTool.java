package mr.demonid.pk8000.ru.util;


import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;


/**
 * Хелпер для работы с путями в программе.
 * Приводит их в корректный системный вид.
 */
@Component
@Log4j2
public class PathTool {

    private Path rootPath;
    private final Path staticPath;
    private final Path menuIconPath;
    private final Path tempPath;

    private final Path softPath;
    private final String softImagesSubdir;
    private final String softFilesSubdir;
    private final String softDescSubdir;
    private final String softDescAttach;


    public PathTool(AppConfiguration config) {
        findRootPath();
        log.info("Root path: {}", rootPath);

        var map = config.getAliasPaths();
        staticPath = createAbsolutePath(map.get("static-path"));
        softPath = createAbsolutePath(map.get("soft-path"));
        menuIconPath = createAbsolutePath(map.get("menu-icon-path"));
        tempPath = createAbsolutePath(config.getTempDirectory());

        softImagesSubdir = toSystemPath(map.get("soft-images-subdir"));
        softFilesSubdir = toSystemPath(map.get("soft-files-subdir"));
        softDescSubdir = toSystemPath(map.get("soft-desc-subdir"));
        softDescAttach = toSystemPath(map.get("soft-desc-attach"));
    }

    /**
     * Абсолютный путь до корня проекта.
     */
    public Path getRootPath() {
        return rootPath;
    }

    /**
     * Абсолютный путь до папки статичных страниц.
     */
    public Path getStaticPath() {
        return staticPath;
    }

    /**
     * Абсолютный путь до папки с иконками для меню.
     */
    public Path getMenuIconPath() {
        return menuIconPath;
    }

    /**
     * Абсолютный путь до папки под временные файлы.
     */
    public Path getTempPath() {
        return tempPath;
    }

    /**
     * Абсолютный путь до папки динамических страниц (софта).
     */
    public Path getSoftPath() {
        return softPath;
    }

    /**
     * Название папки с изображениями для софта.
     */
    public String getSoftImagesSubdir() {
        return softImagesSubdir;
    }

    /**
     * Название папки с файлами (архивами) для софта.
     */
    public String getSoftFilesSubdir() {
        return softFilesSubdir;
    }

    /**
     * Название папки с описаниями софта.
     */
    public String getSoftDescSubdir() {
        return softDescSubdir;
    }

    /**
     * Название папки с вложениями для описаний софта.
     */
    public String getSoftDescAttach() {
        return softDescAttach;
    }

    /**
     * Абсолютный путь до папки с изображениями для софта.
     */
    public Path getSoftImagesSubdirPath() {
        return toSystemPath(softPath.resolve(softImagesSubdir).normalize());
    }

    /**
     * Абсолютный путь до папки с файлами (архивами) для софта.
     */
    public Path getSoftFilesSubdirPath() {
        return toSystemPath(softPath.resolve(softFilesSubdir).normalize());
    }

    /**
     * Абсолютный путь до папки с описаниями софта.
     */
    public Path getSoftDescSubdirPath() {
        return toSystemPath(softPath.resolve(softDescSubdir).normalize());
    }

    /**
     * Абсолютный путь до папки с вложениями для описаний софта.
     */
    public Path getSoftDescAttachPath() {
        return getSoftDescSubdirPath().resolve(softDescAttach).normalize();
    }


    /**
     * Заменяет слеши на используемые в текущей системе.
     */
    public Path toSystemPath(Path path) {
        return Paths.get(toSystemPath(path.toString()));
    }

    public String toSystemPath(String path) {
        if (path == null || path.startsWith("\\\\")) {
            return path; // или null, или сетевой виндозный путь
        }
        return path.replace("/", File.separator);

    }

    /**
     * Построение абсолютного пути, относительно корня проекта.
     *
     * @param relativePath Путь, относительно корня проекта.
     */
    public Path createAbsolutePath(String relativePath) {
        if (relativePath != null && !relativePath.isBlank()) {
            Path path = Path.of(relativePath);
            return toSystemPath((path.isAbsolute() ? path : rootPath.resolve(relativePath)).normalize());
        }
        return rootPath;
    }

    public Path createAbsolutePath(Path root, Path relativePath) {
        if (relativePath != null && root != null && !root.isAbsolute()) {
            return toSystemPath((relativePath.isAbsolute() ? relativePath : root.resolve(relativePath)).normalize());
        }
        return rootPath;
    }


    /**
     * Удаляет в каталоге все пустые подкаталоги.
     * @param baseDir Базовый каталог. Тоже удалится, если в нем не будет файлов.
     */
    public void cleanDirectory(Path baseDir) throws ServiceException {
        if (baseDir == null || !Files.exists(baseDir) || !Files.isDirectory(baseDir))
            return;

        // Проходим по всем каталогам, начиная с самых глубоких
        try (Stream<Path> walk = Files.walk(baseDir)) {
            walk.sorted(Comparator.reverseOrder())
                    .filter(Files::isDirectory)
                    .forEach(dir -> {
                        try (Stream<Path> contents = Files.list(dir)) {
                            if (contents.findAny().isEmpty()) {
                                Files.deleteIfExists(dir);
                                log.info("Empty directory removed: '{}'", dir);
                            }
                        } catch (IOException e) {
                            log.error("Ошибка при проверке каталога '{}': {}", dir, e.getMessage());
                        }
                    });
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.IO_ERROR, "cleanDirectory: " + e.getMessage());
        }
    }


    @Override
    public String toString() {
        return "PathTool {" + "\n" +
                "rootPath=" + rootPath + "\n" +
                ", staticPath=" + staticPath + "\n" +
                ", menuIconPath=" + menuIconPath + "\n" +
                ", tempPath=" + tempPath + "\n" +
                ", softPath=" + softPath + "\n" +
                ", softImagesSubdir='" + softImagesSubdir + '\'' + "\n" +
                ", softFilesSubdir='" + softFilesSubdir + '\'' + "\n" +
                ", softDescSubdir='" + softDescSubdir + '\'' + "\n" +
                ", softDescAttach='" + softDescAttach + '\'' + "\n" +
                '}';
    }



    /**
     * Определяем путь к папке проекта/jar-файла.
     */
    private void findRootPath() {
        ApplicationHome home = new ApplicationHome(PathTool.class);
        File source = home.getSource();
        if (source != null && source.isFile() && source.getName().endsWith(".jar")) {
            rootPath = Paths.get(home.getDir().getAbsolutePath());
        } else {
            rootPath = Paths.get(System.getProperty("user.dir")).toAbsolutePath();
        }
    }


}
