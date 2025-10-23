package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import mr.demonid.pk8000.ru.services.menu.MenuItem;
import mr.demonid.pk8000.ru.services.menu.MenuService;
import mr.demonid.pk8000.ru.util.AuthUtil;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
@AllArgsConstructor
public class FullPageHelper {

    private final MenuService menuService;

    public String renderFullPage(String path, Model model) {
        List<MenuItem> menu = menuService.buildMenu(AuthUtil.isAdmin());
        model.addAttribute("menu", menu);
        model.addAttribute("initialPath", path);
        return "main";
    }

}