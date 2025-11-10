package mr.demonid.pk8000.ru.configs;

import mr.demonid.pk8000.ru.util.AliasPaths;
import mr.demonid.pk8000.ru.util.PathTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PathsInitialize {

//    @Bean
//    public PathUtil pathUtil() {
//        return new PathUtil();
//    }

    @Bean
    public AliasPaths aliasPaths(AppConfiguration config) {
        var map = config.getAliasPaths();
        return new AliasPaths(
                map.get("static-url"),
                map.get("soft-url"),
                map.get("soft-images-subdir"),
                map.get("soft-files-subdir"),
                map.get("soft-desc-subdir"),
                map.get("menu-icon-url")
        );
    }

    //    @DependsOn("pathUtil")
    @Bean
    public PathTool pathTool(AppConfiguration config) {
        return new PathTool(config);
    }

}
