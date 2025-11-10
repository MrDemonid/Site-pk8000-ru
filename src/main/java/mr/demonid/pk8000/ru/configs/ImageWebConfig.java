package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.util.AliasPaths;
import mr.demonid.pk8000.ru.util.PathTool;
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
    private PathTool pathTool;


    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        registryPath(registry, aliasPaths.menuIconUrl(), pathTool.getMenuIconPath());   // menu/icons -> ./content/icons/menu/
        registryPath(registry, aliasPaths.staticUrl(), pathTool.getStaticPath());       // attache -> ./content/menu/
        registryPath(registry, aliasPaths.softUrl(), pathTool.getSoftPath());           // soft -> ./content/soft/
    }


    private void registryPath(ResourceHandlerRegistry registry, String url, Path path) {
        if (path == null || path.toString().isBlank()) {
            log.error("ImageWebConfig: path of 'from' is empty!");
            return;
        }
        String fullPath = path.toString() + Paths.get(File.separator);
        log.info("Resource: /{}/** -> {}", url, fullPath);
        // добавляем переадресацию для путей
        registry.addResourceHandler("/" + url + "/**")
                .addResourceLocations("file:" + fullPath)
                .setCachePeriod(3600);
    }

}