package mr.demonid.pk8000.ru.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.controller.test.Product;
import mr.demonid.pk8000.ru.services.menu.MenuService;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;


@Controller
@AllArgsConstructor
@Log4j2
public class AppController {

//    private final OldMenuService oldMenuService;
    private MenuService menuService;


    @GetMapping("/")
    public String index(@RequestParam(value = "path", required = false) String path,
                        @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
                        @RequestParam(defaultValue = "") String q,
                        Model model, Authentication auth) throws IOException {
        if (path == null) {
            path = "/api/v1/index";
        }
        Page<Product> pageResult = new PageImpl<>(
                new ArrayList<>(),
                pageable,
                0
        );
        model.addAttribute("initialPath", path);
        model.addAttribute("pageData", pageResult);
//        model.addAttribute("menu", oldMenuService.getMenu(auth));
        model.addAttribute("menu", menuService.buildMenu());
        return "main";
//        return "redirect:/index";
    }

}
