package mr.demonid.pk8000.ru.util;


/**
 * Пути к ресурсам и их псевдонимы.
 *
 * @param staticUrl        Виртуальный путь к статичным ресурсам.
 * @param softUrl          Виртуальный путь к динамически формируемым страницам (раздел Soft).
 * @param softImagesSubdir Подкаталог для изображений динамических страниц.
 * @param softFilesSubdir  Подкаталог для архивов динамических страниц.
 * @param softDescSubdir   Подкаталог для описаний программ динамических страниц.
 * @param menuIconUrl      Виртуальный путь до иконок меню.
 */
public record AliasPaths(
        String staticUrl,
        String softUrl,
        String softImagesSubdir,
        String softFilesSubdir,
        String softDescSubdir,
        String menuIconUrl) {
}


