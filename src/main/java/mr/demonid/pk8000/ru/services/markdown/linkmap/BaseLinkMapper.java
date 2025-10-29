package mr.demonid.pk8000.ru.services.markdown.linkmap;


import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.util.PathUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


@Log4j2
public class BaseLinkMapper implements LinkMappingStrategy {

    private final String aliasUrl;
    private final String endpoint;
    private final String currentPath;


    /**
     *
     * @param aliasUrl   виртуальный путь, заменяющий базовую папку.
     * @param currentPath Путь относительно базовой папки.
     */
    public BaseLinkMapper(String aliasUrl, String endpoint, String currentPath) {
        this.aliasUrl = aliasUrl.replaceFirst("^/", "");
        this.endpoint = endpoint;
        this.currentPath = currentPath.replaceFirst("^/", "");
    }


    @Override
    public String mapLink(String href) {
        // проверяем, это ссылка на документ или на ресурс
        if (href.endsWith(".md")) {
            // это ссылка на конкретный файл *.md, поэтому подставляем переход на страницу
            Path base = Paths.get(currentPath);
            String resolved = PathUtil.normalize(base.resolve(href).normalize().toString(), true);
            resolved = resolved.replaceFirst("/?[^/]+\\.md$", "");
            return endpoint + resolved;
        }

        // это ссылка на рисунок или архив
        Path path = Path.of(
                        aliasUrl,
                        currentPath,
                        href.replaceFirst("^/", ""))                    // images/bk.png
                .normalize();
        return PathUtil.normalize(File.separator + path, true);
    }

}
