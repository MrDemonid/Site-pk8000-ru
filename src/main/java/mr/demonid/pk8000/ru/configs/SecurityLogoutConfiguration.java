package mr.demonid.pk8000.ru.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;


/**
 * Выход из Keycloak.
 */
@Configuration
public class SecurityLogoutConfiguration {

    @Bean
    public OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler(
            ClientRegistrationRepository clientRegistrationRepository) {

        OidcClientInitiatedLogoutSuccessHandler successHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Указываем адрес, куда будет редирект после logout у провайдера
        successHandler.setPostLogoutRedirectUri("{baseUrl}/");

        return successHandler;
    }
}