package mr.demonid.pk8000.ru.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@AllArgsConstructor
@Log4j2
public class LoginController {

    private final AppConfiguration appConfig;
    private final OAuth2ClientProperties oAuth2ClientProperties;


    @GetMapping("/enter")
    public String login() {
        // Получаем первый зарегистрированный клиент
        String registrationId = oAuth2ClientProperties.getRegistration()
                .keySet()
                .iterator()
                .next();
        log.info("Login with registration id '{}'", registrationId);
        return "redirect:/oauth2/authorization/" + registrationId;
    }
}
