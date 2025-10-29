package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * Переадресация запросов на ресурсы, в данном случае на картинки и иконки.
 * Путь к ресурсу в SecurityConfig должен быть выставлен только для чтения!!!
 * Например: .requestMatchers(HttpMethod.GET, "/путь/**").permitAll()
 */
@Configuration
@AllArgsConstructor
@Log4j2
public class ImageWebConfig implements WebMvcConfigurer {

    private AliasPaths aliasPaths;


    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registryPath(registry, aliasPaths.menuIconUrl(), aliasPaths.menuIconPath());    // menu/icons -> ./content/icons/menu/
        registryPath(registry, aliasPaths.staticUrl(), aliasPaths.staticPath());        // attache -> ./content/menu/
        registryPath(registry, aliasPaths.softUrl(), aliasPaths.softPath());            // soft -> ./content/soft/
    }


    private void registryPath(ResourceHandlerRegistry registry, String url, String path) {
        Path root = PathUtil.getRootPath();
        if (path != null && !path.isBlank()) {
            root = Paths.get(root.toString(), path);
        } else {
            log.error("ImageWebConfig: path of 'from' is empty!");
            return;
        }
        String fullPath = root.normalize().toString() + Paths.get(File.separator);
        log.info("Resource: {} -> {}", "/" + url + "/**", fullPath);
        // добавляем переадресацию для путей
        registry.addResourceHandler("/" + url + "/**")
                .addResourceLocations("file:" + fullPath)
                .setCachePeriod(3600);
    }

}