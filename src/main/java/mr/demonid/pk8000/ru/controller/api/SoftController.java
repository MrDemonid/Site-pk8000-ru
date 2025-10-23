package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.CategoryGroup;
import mr.demonid.pk8000.ru.domain.CategoryType;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.services.SoftService;
import mr.demonid.pk8000.ru.services.menu.MenuProperties;
import mr.demonid.pk8000.ru.util.pagenate.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class SoftController {

    private final SoftService softService;
    private MenuProperties menuProperties;


    @GetMapping("/soft/games/all")
    public String allGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        List<CategoryType> gameCategories = Arrays.stream(CategoryType.values())
                .filter(c -> c.getGroup() == CategoryGroup.GAMES)
                .toList();
        return makeSoftFragment(gameCategories, pageable, q, model);
    }

    @GetMapping("/soft/games/arcade")
    public String arcadeGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.ARCADE), pageable, q, model);
    }

    @GetMapping("/soft/games/car")
    public String racingGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.RACING), pageable, q, model);
    }

    @GetMapping("/soft/games/sport")
    public String sportGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.SPORT), pageable, q, model);
    }

    @GetMapping("/soft/games/logic")
    public String logicGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.LOGIC), pageable, q, model);
    }

    @GetMapping("/soft/games/edu")
    public String eduGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.EDU), pageable, q, model);
    }

    @GetMapping("/soft/games/other")
    public String otherGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.OTHER_GAMES), pageable, q, model);
    }

    @GetMapping("/soft/all")
    public String allSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        List<CategoryType> softCategories = Arrays.stream(CategoryType.values())
                .filter(c -> c.getGroup() == CategoryGroup.SOFTWARE)
                .toList();
        return makeSoftFragment(softCategories, pageable, q, model);
    }

    @GetMapping("/soft/tools")
    public String toolsSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.TOOLS), pageable, q, model);
    }

    @GetMapping("/soft/system")
    public String systemSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.SYSTEM), pageable, q, model);
    }

    @GetMapping("/soft/code")
    public String programmingSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.PROGRAMMING), pageable, q, model);
    }

    @GetMapping("/soft/other")
    public String otherSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(List.of(CategoryType.OTHER_SOFTWARE), pageable, q, model);
    }



    private String makeSoftFragment(List<CategoryType> type, Pageable pageable, String q, Model model) {

        List<Long> categories = type.stream()
                .map(menuProperties::getCategoryId)
                .toList();

        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
        Page<SoftResponse> soft = softService.getAllProducts(new SoftFilter(categories, q), page);
        model.addAttribute("softList", soft.getContent());
        model.addAttribute("q", q);
        // пагинатор
        PaginationHelper.setPaginator(soft, model);
        return "fragments/soft-list :: softListFragment";
    }


}
