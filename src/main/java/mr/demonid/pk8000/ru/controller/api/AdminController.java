package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.CategoryType;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/v1/admin")
public class AdminController {

    private AdminServiceImpl adminService;


    @GetMapping("/soft")
    String doSoft(Model model, Pageable pageable) {
        Page<SoftResponse> soft = adminService.getAllProducts(null, pageable);
        System.out.println("-- soft: " + soft.getContent());
        model.addAttribute("softList", soft.getContent());
        model.addAttribute("categories", CategoryType.values());
        return "fragments/soft-list-edit :: softListEdit";
    }

}
