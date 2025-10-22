package mr.demonid.pk8000.ru.controller;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.menu.MenuItem;
import mr.demonid.pk8000.ru.services.menu.MenuService;
import mr.demonid.pk8000.ru.util.AuthUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;


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

        List<String> scopes = AuthUtil.getCurrentUserAuthorities();

        if (path == null) {
            path = "/api/v1/page?path=main";
        }
        List<MenuItem> menu = filterMenu(menuService.buildMenu(), AuthUtil.isAdmin());

        model.addAttribute("initialPath", path);
        model.addAttribute("menu", menu);
        return "main";
    }

    @GetMapping("/index")
    public String index(@RequestParam(value = "path", required = false) String path, Model model) throws IOException {
        return "redirect:/";
    }


    /**
     * Отсеивает пункты меню в соответствии с уровнем текущей роли.
     */
    private List<MenuItem> filterMenu(List<MenuItem> menu, boolean isAdmin) {
        if (menu == null)
            return List.of();

        return menu.stream()
                .filter(item -> !item.isAdminOnly() || isAdmin)
                .map(item -> {
                    MenuItem copy = new MenuItem();
                    BeanUtils.copyProperties(item, copy);
                    if (item.getChildren() != null) {
                        copy.setChildren(filterMenu(item.getChildren(), isAdmin));
                    }
                    return copy;
                })
                .toList();
    }

}
