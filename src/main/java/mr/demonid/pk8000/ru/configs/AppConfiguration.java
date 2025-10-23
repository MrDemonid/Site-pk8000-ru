package mr.demonid.pk8000.ru.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfiguration {
    private String realmName;
    private String authServerUrl;

    private String staticEndpoint;
    private String softEndpoint;

    private String menuIconPath;        // каталог иконок для меню
    private String menuIconUrl;         // виртуальный каталог иконок (для html)
    private String contentPath;         // каталог контента статичного меню
    private String attacheUrl;          // виртуальный каталог для контента статичного меню (для html)
    private String softImagesPath;      // каталог картинок софта
    private String softImagesUrl;       // виртуальный каталог картинок софта (для html)
    private String softFilesPath;       // каталог файлов софта
    private String softFilesUrl;        // виртуальный каталог файлов софта (для html)

    private String tempDirectory;
}
