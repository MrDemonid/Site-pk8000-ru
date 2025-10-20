package mr.demonid.pk8000.ru.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Service
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
