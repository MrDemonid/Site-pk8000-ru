package mr.demonid.pk8000.ru.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.services.markdown.MarkdownService;
import mr.demonid.pk8000.ru.services.menu.MenuProperties;
import mr.demonid.pk8000.ru.util.PathTool;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@AllArgsConstructor
@Log4j2
public class PageService {

    private static final String NOT_FOUND_PAGE = """
            ## Error.
            Страница не найдена.
            """;

    private MarkdownService markdownService;
    private MenuProperties menuProperties;
    private PathTool pathTool;


    /**
     * Загрузка страницы формата markdown и её конвертация в HTML.
     */
    public String getPage(String path) {

        // строим путь до ресурса
        if (path == null || path.isBlank()) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Путь не может быть пустым");
        }
        path = PathUtil.toSystemPath(path).toString();
        Path filePath = pathTool.getStaticPath().resolve(path).resolve(menuProperties.getIndexFile());

        try {
            // считываем и конвертируем в html
            String md = Files.readString(filePath);
            return markdownService.toHtmlStatic(md, Paths.get(File.separator).resolve(path).normalize().toString());

        } catch (IOException e) {
            // ресурс не найден
            log.error("PageService.getPage('{}') can't load: {}", path, e.getMessage());
            return markdownService.toHtmlStatic(NOT_FOUND_PAGE, "");
        }
    }

}
