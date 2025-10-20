package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.PageService;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    private PageService pageService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path iconsPath = PathUtil.getRootPath();
        if (appConfig.getIconPath() != null && !appConfig.getIconPath().isBlank()) {
            iconsPath = Paths.get(iconsPath.toString(), appConfig.getIconPath());
        } else {
            iconsPath = Paths.get("./images/icons/");
        }
        Path attachPath = PathUtil.getRootPath();
        if (appConfig.getIconPath() != null && !appConfig.getContentPath().isBlank()) {
            attachPath = Paths.get(attachPath.toString(), appConfig.getContentPath());
        } else {
            attachPath = Paths.get(".");
        }

        String finalIconsPath = iconsPath.normalize().toString() + Paths.get("/");
        log.info("Resource /icons path: {}", finalIconsPath);
        // добавляем переадресацию для путей "/icons/*"
        registry.addResourceHandler("/icons/**")
                .addResourceLocations("file:" + finalIconsPath, "classpath:/static/icons/")
                .setCachePeriod(3600);

        String finalAttachPath = attachPath.normalize().toString() + Paths.get("/");
        log.info("Resource /attache path: {}", finalAttachPath);
        registry.addResourceHandler("/attache/**")
                .addResourceLocations("file:" + finalAttachPath)
                .setCachePeriod(3600);
        // registry.addResourceHandler("/media/**")
    }

}