package mr.demonid.pk8000.ru.services.menu;

import lombok.Getter;
import lombok.Setter;
import mr.demonid.pk8000.ru.domain.CategoryType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "menu")
public class MenuProperties {
    private String metaFile;
    private String indexFile;
    private Map<CategoryType, Integer> category;
}