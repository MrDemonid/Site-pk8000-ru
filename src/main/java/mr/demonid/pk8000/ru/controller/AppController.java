package mr.demonid.pk8000.ru.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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


    @GetMapping("/")
    public String main(@RequestParam(value = "path", required = false) String path, Model model) throws IOException {
        if (path == null) {
            path = "/api/v1/page?path=main";
        }
        model.addAttribute("initialPath", path);
        model.addAttribute("menu", menuService.buildMenu());
        return "main";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "path", required = false) String path, Model model) throws IOException {
        return "redirect:/";
    }

}
