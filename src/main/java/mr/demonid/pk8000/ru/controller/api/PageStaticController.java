package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Контроллер для статических страниц формата markdown.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class PageStaticController {

    private final PageService pageService;


    @GetMapping("/page")
    String page(@RequestParam(value = "path", required = false) String path, Model model) {
//        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        System.out.println("GET /PAGE?path=" + path);
        if (path == null) {
            path = "main";
        }
        model.addAttribute("content", pageService.getPage(path));
        return "fragments/document-view :: documentView";
    }


}
