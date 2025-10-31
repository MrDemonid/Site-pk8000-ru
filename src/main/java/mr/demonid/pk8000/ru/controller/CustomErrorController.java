package mr.demonid.pk8000.ru.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Log4j2
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        // детали ошибки
        Object errorType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        int statusCode = statusAttr != null ? Integer.parseInt(statusAttr.toString()) : 500;
        String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        if (uri == null)
            uri = request.getRequestURI();

        // базовая информация
        model.addAttribute("status", statusCode);
        model.addAttribute("path", uri);
        model.addAttribute("method", request.getMethod());

        if (errorType != null) {
            model.addAttribute("error", errorType.toString());
        } else if (exception != null) {
            model.addAttribute("error", exception.getClass().getSimpleName());
        } else {
            model.addAttribute("error", "—");
        }

        model.addAttribute("message", errorMessage != null ? errorMessage.toString() : "—");

        log.error("Code {}: {} ({})", statusCode, uri, errorMessage);

        // выбор страницы
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return "error/404";
        } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
            return "error/403";
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return "error/500";
        }
        return "error/default";
    }
//    @RequestMapping("/error")
//    public String handleError(HttpServletRequest request, Model model) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        Object errorType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
//        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
//        Object errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
//
//        int statusCode = Integer.parseInt(status.toString());
//
//        if (errorType != null) {
//            model.addAttribute("error", errorType.toString());
//        } else if (exception != null) {
//            model.addAttribute("error", exception.getClass().getSimpleName());
//        } else {
//            model.addAttribute("error", "Unknown Error");
//        }
//
//        String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
//        if (uri == null) {
//            uri = request.getRequestURI();
//        }
//        model.addAttribute("status", status);
//        model.addAttribute("path", uri);
//        model.addAttribute("method", request.getMethod());
//
//        if (status != null) {
//
//            System.out.println("ERROR STATUS CODE: " + statusCode);
//
//            if (statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error/404";
//            }
//            if (statusCode == HttpStatus.FORBIDDEN.value()) {
//                return "error/403";
//            }
//        }
//
//        model.addAttribute("message", errorMessage);
//        return "error/default";
//    }
}
