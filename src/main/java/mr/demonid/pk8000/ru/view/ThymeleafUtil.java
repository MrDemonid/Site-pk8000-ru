package mr.demonid.pk8000.ru.view;

import org.springframework.stereotype.Component;


/**
 * Вспомогательные методы для Thymeleaf-шаблонов.
 */
@Component("thymeleafUtil")
public class ThymeleafUtil {

    /**
     * Возвращает имя файла из URL без пути и версии (?v=xxx).
     * Пример: "/catalog/api/product/42/archive/fm.zip?v=3" -> "fm.zip"
     */
    public String fileName(String url) {
        if (url == null || url.isBlank()) {
            return "";
        }
        // получаем имя файла
        int slashIndex = url.lastIndexOf('/');
        String file = (slashIndex != -1) ? url.substring(slashIndex + 1) : url;

        // отрезаем версию после '?'
        int qIndex = file.indexOf('?');
        return (qIndex != -1) ? file.substring(0, qIndex) : file;
    }

    /**
     * Возвращает имя файла без расширения.
     * Пример: "/path/image.png?v=2" -> "image"
     */
    public String fileNameWithoutExtension(String url) {
        String name = fileName(url);
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex != -1) ? name.substring(0, dotIndex) : name;
    }

    /**
     * Возвращает расширение файла (без точки), учитывая версию.
     * Пример: "/path/image.png?v=2" -> "png"
     */
    public String fileExtension(String url) {
        String name = fileName(url);
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex != -1) ? name.substring(dotIndex + 1) : "";
    }

}