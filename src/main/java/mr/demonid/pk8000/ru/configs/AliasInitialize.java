package mr.demonid.pk8000.ru.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AliasInitialize {

    @Bean
    public AliasPaths aliasPaths(AppConfiguration config) {
        var map = config.getAliasPaths();
        return new AliasPaths(
                map.get("static-url"),
                map.get("static-path"),
                map.get("soft-url"),
                map.get("soft-path"),
                map.get("soft-images-subdir"),
                map.get("soft-files-subdir"),
                map.get("soft-desc-subdir"),
                map.get("menu-icon-url"),
                map.get("menu-icon-path")
        );
    }

}
