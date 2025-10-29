package mr.demonid.pk8000.ru.configs;


/**
 * Пути к ресурсам и их псевдонимы.
 * @param staticUrl    Виртуальный путь к статичным ресурсам.
 * @param staticPath   Физический путь на диске до основного каталога к статичным ресурсам.
 * @param softUrl      Виртуальный путь к динамически формируемым страницам (раздел Soft).
 * @param softPath     Физический путь на диске до основного каталога к ресурсам динамических страниц.
 * @param menuIconUrl  Виртуальный путь до иконок меню.
 * @param menuIconPath Физический путь на диске до основного каталога с иконками для меню.
 */
public record AliasPaths(
        String staticUrl,
        String staticPath,
        String softUrl,
        String softPath,
        String menuIconUrl,
        String menuIconPath
) {}
