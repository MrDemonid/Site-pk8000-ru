package mr.demonid.pk8000.ru.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.controller.api.FullPageHelper;
import mr.demonid.pk8000.ru.services.menu.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;


/**
 * Точка входа на сайт, переадресовываем на главную страницу.
 */
@Controller
@AllArgsConstructor
@Log4j2
public class AppController {

    private final MenuService menuService;
    private final AppConfiguration config;
    private final FullPageHelper fullPageHelper;


    @GetMapping("/")
    public String mainPage(@RequestParam(value = "path", required = false) String path, Model model) {
        if (path == null) {
            path = config.getStaticEndpoint() + "/main";
        }
        return fullPageHelper.renderFullPage(path, model);
    }


    @GetMapping("/index")
    public String index() throws IOException {
        return "redirect:/";
    }

}
