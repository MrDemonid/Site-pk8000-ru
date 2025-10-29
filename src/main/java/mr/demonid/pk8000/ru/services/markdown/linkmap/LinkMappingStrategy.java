package mr.demonid.pk8000.ru.services.markdown.linkmap;


/**
 * Интерфейс для конвертации путей в ссылку.
 */
public interface LinkMappingStrategy {

    /**
     * Формирует ссылку на подключаемый файл (рисунок, архив)
     * @param href Путь до файла (относительно папки документа).
     */
    String mapLink(String href);
}
