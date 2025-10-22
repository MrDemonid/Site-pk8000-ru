package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private AppConfiguration config;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // Разрешаем CORS
                .csrf(AbstractHttpConfigurer::disable)                      // Отключаем CSRF для запросов API
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/", "/index").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/css/**",
                                "/icons/**",
                                "/js/**",
                                "/" + config.getMenuIconUrl() + "/**",
                                "/" + config.getAttacheUrl() + "/**",
                                "/" + config.getSoftImagesUrl() + "/**",
                                "/" + config.getSoftFilesUrl() + "/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN", "DEVELOPER")
                        .anyRequest().authenticated()  // Остальные требуют аутентификации
                )
//                .anonymous(Customizer.withDefaults()) // Включение анонимных пользователей
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(new KeycloakOidcUserService())
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/logout")                // URL выхода
                        .logoutSuccessUrl("/")                      // куда редиректить после выхода
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "ANON_ID")     // удаляем нужные куки
                        .clearAuthentication(true)
                );

        return http.build();
    }


}
