package mr.demonid.pk8000.ru.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.exceptions.ErrorResponse;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Универсальный метод для обработки ServiceException
     */
    @ExceptionHandler(ServiceException.class)
    public Object handleServiceException(ServiceException e, HttpServletRequest request, Model model) {

        ErrorResponse errorResponse = parseErrorResponse(e, request);
        log.error("Ошибка в контроллере: {}", errorResponse);

        // Проверяем, был ли AJAX-запрос
        if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
            // Для AJAX-запроса возвращаем JSON (как у @RestController)
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
        // Для обычного запроса возвращаем HTML-фрагмент
        model.addAttribute("error", errorResponse);
        return "templates/error/error :: errorFragment";
    }

    /**
     * Парсит сообщение ServiceException в ErrorResponse, если возможно
     */
    private ErrorResponse parseErrorResponse(ServiceException e, HttpServletRequest request) {
        String message = e.getMessage();
        if (message != null && message.trim().startsWith("{")) {
            try {
                ErrorResponse nested = objectMapper.readValue(message, ErrorResponse.class);
                log.error("Проброс ошибки из предыдущего сервиса: {}", nested);
                return nested;
            } catch (Exception ex) {
                log.warn("Не удалось распарсить message как ErrorResponse: {}", message);
            }
        }

        // Если не удалось распарсить, создаём стандартный объект ошибки
        return new ErrorResponse(
                LocalDateTime.now(),
                e.getCode(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                message,
                request.getRequestURI()
        );
    }
}