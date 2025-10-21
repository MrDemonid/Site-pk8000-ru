package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.util.PathUtil;
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

    private AppConfiguration appConfig;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registryPath(registry, appConfig.getMenuIconPath(), appConfig.getMenuIconUrl());
        registryPath(registry, appConfig.getContentPath(), appConfig.getAttacheUrl());
        registryPath(registry, appConfig.getSoftImagesPath(), appConfig.getSoftImagesUrl());
        registryPath(registry, appConfig.getSoftFilesPath(), appConfig.getSoftFilesUrl());
    }


    private void registryPath(ResourceHandlerRegistry registry, String path, String url) {
        Path root = PathUtil.getRootPath();
        if (path != null && !path.isBlank()) {
            root = Paths.get(root.toString(), path);
        } else {
            log.error("ImageWebConfig: path of 'from' is empty!");
            return;
        }
        String finalIconsPath = root.normalize().toString() + Paths.get(File.separator);
        log.info("Resource: {} -> {}", "/" + url + "/**", finalIconsPath);
        // добавляем переадресацию для путей
        registry.addResourceHandler("/" + url + "/**")
                .addResourceLocations("file:" + finalIconsPath)
                .setCachePeriod(3600);
    }

}