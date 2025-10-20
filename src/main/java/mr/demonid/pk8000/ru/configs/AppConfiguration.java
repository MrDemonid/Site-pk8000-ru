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
    private String cookieAnonId;
    private String claimUserId;

    private String iconPath;
    private String contentPath;
    private String attacheDir;
    private String staticEndpoint;
}
