package mr.demonid.pk8000.ru.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfiguration {
    private String realmName;
    private String authServerUrl;

    private String staticEndpoint;
    private String softEndpoint;

    private String tempDirectory;

    private Map<String, String> aliasPaths;


}
