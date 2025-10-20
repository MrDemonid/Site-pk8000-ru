package mr.demonid.pk8000.ru.controller.test;

import jakarta.servlet.http.HttpServletRequest;
import mr.demonid.pk8000.ru.util.pagenate.PaginationHelper;
import org.springframework.data.domain.*;
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
@RequestMapping("/api/v1")
public class TestController {


    @GetMapping("/index")
    public String doIndex(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return retFragment("Main", "Index", pageable, q, model);
    }

    @GetMapping("/contacts")
    public String list(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return retFragment("Contacts", "Category", pageable, q, model);
    }

//    @GetMapping("/about/history")
//    public String aboutHistory(
//            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
//            @RequestParam(defaultValue = "") String q,
//            Model domain, Authentication auth, HttpServletRequest request) {
//        return retFragment("About", "History", pageable, q, domain);
//    }

    @GetMapping("/about/sura")
    public String func1(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("About", "Sura", pageable, q, model);
    }

    @GetMapping("/about/vesta")
    public String func2(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("About", "Vesta", pageable, q, model);
    }

    @GetMapping("/about/hobby")
    public String func3(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("About", "Hobbi", pageable, q, model);
    }

    @GetMapping("/about/articles")
    public String func4(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("About", "Articles", pageable, q, model);
    }

    @GetMapping("/docs/sura")
    public String func5(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Docs", "Sura", pageable, q, model);
    }

    @GetMapping("/docs/vesta")
    public String func6(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Docs", "Vesta", pageable, q, model);
    }

    @GetMapping("/docs/hobby")
    public String func7(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Docs", "Hobbi", pageable, q, model);
    }

    @GetMapping("/docs/schemes")
    public String func8(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Docs", "Schemes", pageable, q, model);
    }

    @GetMapping("/docs/cp-m")
    public String func9(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Docs", "CP/M", pageable, q, model);
    }

    @GetMapping("/docs/pl-m")
    public String func10(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Docs", "PL/M", pageable, q, model);
    }

    @GetMapping("/hardware/tv-adapter")
    public String func11(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "TV-adapter", pageable, q, model);
    }

    @GetMapping("/hardware/pal-tv")
    public String func12(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "Pal-TV", pageable, q, model);
    }

    @GetMapping("/hardware/ega")
    public String func13(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "EGA-adapter", pageable, q, model);
    }

    @GetMapping("/hardware/AY-LUT")
    public String func14(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "AY-LUT", pageable, q, model);
    }

    @GetMapping("/hardware/AY-ver12")
    public String func15(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "AY-factory", pageable, q, model);
    }

    @GetMapping("/hardware/hdd")
    public String func16(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "HDD", pageable, q, model);
    }

    @GetMapping("/hardware/fdd")
    public String func17(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "FDD", pageable, q, model);
    }

    @GetMapping("/hardware/rtc")
    public String func18(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model, Authentication auth, HttpServletRequest request) {
        return retFragment("Hard", "RTC", pageable, q, model);
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

    public void showPagination(Page<?> pageData, Model model) {
        int currentPage = pageData.getNumber();
        int totalPages = pageData.getTotalPages();
        int windowSize = 7;

        int startPage = Math.max(0, currentPage - windowSize / 2);
        int endPage = startPage + windowSize - 1;

        if (endPage >= totalPages) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - windowSize + 1);
        }

        model.addAttribute("pageData", pageData);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

    }
}
