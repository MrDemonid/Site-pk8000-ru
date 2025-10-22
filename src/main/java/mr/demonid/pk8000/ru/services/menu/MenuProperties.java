package mr.demonid.pk8000.ru.services.menu;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.CategoryType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "menu")
@Log4j2
public class MenuProperties {
    private String metaFile;
    private String indexFile;
    private Map<CategoryType, Long> category;

    public Long getCategoryId(CategoryType type) {
        return category.getOrDefault(type, category.get(CategoryType.OTHER_SOFTWARE));
    }

    /**
     * Валидация, что значения в yml и CategoryType совпадают.
     */
    @PostConstruct
    public void validateCategories() {
        for (CategoryType type : CategoryType.values()) {
            if (!category.containsKey(type)) {
                log.error("⚠ Category '{}' missing in application.yml! Using ID of OTHER category.", type.name());
            }
        }
        log.info("✅ Category mapping loaded: {}", category);
    }
}
