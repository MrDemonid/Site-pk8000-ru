package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Контроллер для статических страниц формата markdown.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class PageStaticController {

    private final PageService pageService;


    @GetMapping("/page/{*path}")
    public String pageStatic(@PathVariable("path") String path, Model model) {

        System.out.println("GET /PAGE" + path);
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.isEmpty()) {
            path = "main";
        }
        model.addAttribute("content", pageService.getPage(path));
        return "fragments/document-view :: documentView";
    }

}
