package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Подключаем перехват запросов к эндпоинтам.
 */
@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private AjaxRedirectInterceptor ajaxRedirectInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ajaxRedirectInterceptor);
    }
}
