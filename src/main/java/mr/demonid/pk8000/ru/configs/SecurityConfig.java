package mr.demonid.pk8000.ru.configs;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private AliasPaths aliasPaths;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/index", "/enter", "/error").permitAll()
                        .requestMatchers(
                                "/api/v1/page",
                                "/api/v1/soft",
                                "/api/v1/page/**",
                                "/api/v1/soft/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/css/**",
                                "/icons/**",
                                "/js/**",
                                "/" + aliasPaths.menuIconUrl() + "/**",
                                "/" + aliasPaths.staticUrl() + "/**",
                                "/" + aliasPaths.softUrl() + "/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN", "DEVELOPER")

                        .anyRequest().authenticated()  // Остальные требуют аутентификации
                )
                .anonymous(Customizer.withDefaults()) // Включение анонимных пользователей
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(new KeycloakOidcUserService())
                        )
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                                .logoutSuccessHandler(logoutSuccessHandler)
                                .logoutSuccessUrl("/")                      // куда редиректить после выхода
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID", "ANON_ID")     // удаляем нужные куки
                                .clearAuthentication(true)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            // сообщаем, что такой страницы нет
                            response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        }));

        return http.build();
    }


}
