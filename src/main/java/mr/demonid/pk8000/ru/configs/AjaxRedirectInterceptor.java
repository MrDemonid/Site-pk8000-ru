package mr.demonid.pk8000.ru.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Проверка запросов. Если это не AJAX, то перенаправляем на полную
 * перерисовку страницы, с запрошенным фрагментом.
 * Это поможет корректно рисовать страницы после Ctrl+F5 в браузере.
 */
@Component
public class AjaxRedirectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        String uri = request.getRequestURI();

        if (!isAjax && uri.startsWith("/api/")) {
            /*
                браузер пытается напрямую перейти на /api/**, не даем ему совершить эту глупость.
             */
            String redirectUrl = "/?path=" + URLEncoder.encode(uri, StandardCharsets.UTF_8);
            response.sendRedirect(redirectUrl);
            return false; // прерываем выполнение
        }
        return true; // продолжаем выполнение
    }
}
