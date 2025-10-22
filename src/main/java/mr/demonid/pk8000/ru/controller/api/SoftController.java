package mr.demonid.pk8000.ru.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.controller.test.Product;
import mr.demonid.pk8000.ru.domain.CategoryType;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
import mr.demonid.pk8000.ru.services.menu.MenuProperties;
import mr.demonid.pk8000.ru.util.pagenate.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class SoftController {

    private MenuProperties menuProperties;


    private String makeGameFragment(CategoryType categoryType, Pageable pageable, String q, Model model) {
        return retFragment("Soft/Games", categoryType.name(), pageable, q, model);
    }

    @GetMapping("/soft/games/all")
    public String func19(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Soft/Games", "All", pageable, q, model);
    }

    @GetMapping("/soft/games/arcade")
    public String func20(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return makeGameFragment(CategoryType.ARCADE, pageable, q, model);
    }

    @GetMapping("/soft/games/car")
    public String func21(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return makeGameFragment(CategoryType.RACING, pageable, q, model);
    }

    @GetMapping("/soft/games/sport")
    public String gamesSports(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return makeGameFragment(CategoryType.SPORT, pageable, q, model);
    }

    @GetMapping("/soft/games/logic")
    public String func22(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return makeGameFragment(CategoryType.LOGIC, pageable, q, model);
    }

    @GetMapping("/soft/games/edu")
    public String gamesEdu(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return makeGameFragment(CategoryType.EDU, pageable, q, model);
    }

    @GetMapping("/soft/games/other")
    public String func23(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return makeGameFragment(CategoryType.OTHER, pageable, q, model);
    }

    private AdminServiceImpl adminService;

    @GetMapping("/soft/system")
    public String func24(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {

        Page<SoftResponse> soft = adminService.getAllProducts(null, pageable);
        System.out.println("-- soft: " + soft.getContent());
        model.addAttribute("softList", soft.getContent());
        return "fragments/soft-list :: softListFragment";

//        return retFragment("Soft", "System", pageable, q, model);
    }


    private String retFragment(String name, String category, Pageable pageable, String q, Model model) {
        List<Product> mockProducts = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new Product(i, name + " " + i, category + " " + (i % 5)))
                .toList();

        List<Product> filtered = mockProducts.stream()
                .filter(p -> p.name().toLowerCase().contains(q.toLowerCase()))
                .toList();

        int start = Math.min(pageable.getPageNumber() * pageable.getPageSize(), filtered.size());
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        List<Product> pageContent = filtered.subList(start, end);

        Page<Product> pageResult = new PageImpl<>(
                pageContent,
                pageable,
                filtered.size()
        );

        model.addAttribute("q", q);
        model.addAttribute("products", pageContent);

        // пагинатор
        PaginationHelper.setPaginator(pageResult, model);

        return "fragments/product-list :: productList";
    }

}
