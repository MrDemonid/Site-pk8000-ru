package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.services.PageService;
import mr.demonid.pk8000.ru.services.menu.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;


/**
 * Контроллер для статических страниц формата markdown.
 */
@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/page")
@Log4j2
public class PageStaticController {

    private final PageService pageService;
    private final MenuService menuService;
    private final AppConfiguration config;
    private final FullPageHelper fullPageHelper;


    @GetMapping("/{*path}")
    public String pageStatic(@PathVariable("path") String path, Model model,
                             @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) throws IOException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        String content = pageService.getPage(path);
        model.addAttribute("content", content);

        if (isAjax) {
            return "fragments/document-view :: documentView";
        }
        // это не вызов из JS, нужно полностью загрузить контент сайта
        return fullPageHelper.renderFullPage(config.getStaticEndpoint() + "/" + path, model);
    }

}
