package mr.demonid.pk8000.ru.services;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import mr.demonid.pk8000.ru.services.menu.MenuProperties;
import mr.demonid.pk8000.ru.services.staticpage.MarkdownService;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;


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
    private AppConfiguration appConfiguration;


    /**
     * Загрузка страницы формата markdown и её конвертация в HTML.
     */
    public String getPage(String path) {

        if (path == null || path.isBlank()) {
            throw new ServiceException(ErrorCodes.BAD_DATA, "Путь не может быть пустым");
        }
        path = PathUtil.toSystemPath(path).toString();
        // строим путь до ресурса
        Path filePath = Paths.get(PathUtil.getRootPath().normalize().toString())
                .resolve(appConfiguration.getContentPath())
                .resolve(path)
                .resolve(menuProperties.getIndexFile());

        try {
            // считываем и конвертируем в html
            String md = Files.readString(filePath);
            //            String clean = cleanHtml(html);
            return markdownService.toHtml(md, Paths.get(File.separator).resolve(path).toString());

        } catch (IOException e) {
            // ресурс не найден
            log.error("PageService.getPage('{}') can't load: {}", path, e.getMessage());
            return markdownService.toHtml(NOT_FOUND_PAGE);
        }
    }

    /**
     * Очищает HTML от потенциально опасного кода.
     * @param html Исходный HTML.
     */
    public String cleanHtml(String html) {
        Safelist safe = Safelist.relaxed().addAttributes("span", "style");
        Document doc = Jsoup.parseBodyFragment(html);
        for (Element element : doc.select("[style]")) {
            String style = element.attr("style");
            // удаляем любой стиль, кроме цветовых
            String cleaned = Arrays.stream(style.split(";"))
                    .map(String::trim)
                    .filter(s -> s.matches("(?i)^(color|background-color)\\s*:\\s*#?[a-z0-9()%,\\s]+$"))
                    .collect(Collectors.joining("; "));
            element.attr("style", cleaned);
        }
        return Jsoup.clean(doc.body().html(), safe);
    }
}
