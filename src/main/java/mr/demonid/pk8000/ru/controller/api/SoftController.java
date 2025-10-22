package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.domain.CategoryType;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.services.SoftService;
import mr.demonid.pk8000.ru.services.menu.MenuProperties;
import mr.demonid.pk8000.ru.util.pagenate.PaginationHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1")
@Log4j2
public class SoftController {

    private final SoftService softService;
    private MenuProperties menuProperties;


    @GetMapping("/soft/games/all")
    public String func19(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(null, pageable, q, model);
    }

    @GetMapping("/soft/games/arcade")
    public String func20(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(CategoryType.ARCADE, pageable, q, model);
    }

    @GetMapping("/soft/games/car")
    public String func21(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(CategoryType.RACING, pageable, q, model);
    }

    @GetMapping("/soft/games/sport")
    public String gamesSports(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(CategoryType.SPORT, pageable, q, model);
    }

    @GetMapping("/soft/games/logic")
    public String func22(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(CategoryType.LOGIC, pageable, q, model);
    }

    @GetMapping("/soft/games/edu")
    public String gamesEdu(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(CategoryType.EDU, pageable, q, model);
    }

    @GetMapping("/soft/games/other")
    public String other_games(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {
        return makeSoftFragment(CategoryType.OTHER_GAMES, pageable, q, model);
    }

    @GetMapping("/soft/tools")
    public String tools(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {

        return makeSoftFragment(CategoryType.TOOLS, pageable, q, model);
    }

    @GetMapping("/soft/system")
    public String func24(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {

        return makeSoftFragment(CategoryType.SYSTEM, pageable, q, model);
    }

    @GetMapping("/soft/code")
    public String programming(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {

        return makeSoftFragment(CategoryType.PROGRAMMING, pageable, q, model);
    }

    @GetMapping("/soft/other")
    public String otherSoftware(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String q,
            Model model) {

        return makeSoftFragment(CategoryType.OTHER_SOFTWARE, pageable, q, model);
    }



    private String makeSoftFragment(CategoryType type, Pageable pageable, String q, Model model) {
        Page<SoftResponse> soft = softService.getAllProducts(new SoftFilter(type == null ? null : menuProperties.getCategoryId(type), q), pageable);
        model.addAttribute("softList", soft.getContent());
        model.addAttribute("q", q);
        // пагинатор
        PaginationHelper.setPaginator(soft, model);
        return "fragments/soft-list :: softListFragment";
    }


}
