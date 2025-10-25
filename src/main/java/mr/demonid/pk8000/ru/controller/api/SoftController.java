package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/soft")
@Log4j2
public class SoftController {

    private final SoftService softService;
    private final MenuProperties menuProperties;
    private final FullPageHelper fullPageHelper;
    private final AppConfiguration config;


    @GetMapping("/games/all")
    public String allGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        List<CategoryType> gameCategories = Arrays.stream(CategoryType.values())
                .filter(c -> c.getGroup() == CategoryGroup.GAMES)
                .toList();
        return renderPage("/games/all", gameCategories, requestedWith, pageable, query, model);
    }

    @GetMapping("/games/arcade")
    public String arcadeGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/games/arcade", List.of(CategoryType.ARCADE), requestedWith, pageable, query, model);
    }

    @GetMapping("/games/car")
    public String racingGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/games/car", List.of(CategoryType.RACING), requestedWith, pageable, query, model);
    }

    @GetMapping("/games/sport")
    public String sportGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/games/sport", List.of(CategoryType.SPORT), requestedWith, pageable, query, model);
    }

    @GetMapping("/games/logic")
    public String logicGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/games/logic", List.of(CategoryType.LOGIC), requestedWith, pageable, query, model);
    }

    @GetMapping("/games/edu")
    public String eduGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/games/edu", List.of(CategoryType.EDU), requestedWith, pageable, query, model);
    }

    @GetMapping("/games/other")
    public String otherGames(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/games/other", List.of(CategoryType.OTHER_GAMES), requestedWith, pageable, query, model);
    }

    @GetMapping("/all")
    public String allSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        List<CategoryType> softCategories = Arrays.stream(CategoryType.values())
                .filter(c -> c.getGroup() == CategoryGroup.SOFTWARE)
                .toList();
        return renderPage("/all", softCategories, requestedWith, pageable, query, model);
    }

    @GetMapping("/tools")
    public String toolsSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/tools", List.of(CategoryType.TOOLS), requestedWith, pageable, query, model);
    }

    @GetMapping("/system")
    public String systemSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/system", List.of(CategoryType.SYSTEM), requestedWith, pageable, query, model);
    }

    @GetMapping("/code")
    public String programmingSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/code", List.of(CategoryType.PROGRAMMING), requestedWith, pageable, query, model);
    }

    @GetMapping("/other")
    public String otherSoft(
            @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String query,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Model model) {
        return renderPage("/other", List.of(CategoryType.OTHER_SOFTWARE), requestedWith, pageable, query, model);
    }


    /**
     * В зависимости от типа запроса возвращает либо фрагмент, либо полную страницу.
     */
    private String renderPage(
            String path,
            List<CategoryType> type,
            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
            Pageable pageable,
            String query,
            Model model) {

        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        List<Long> categories = type.stream()
                .map(menuProperties::getCategoryId)
                .toList();

        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
        Page<SoftResponse> soft = softService.getAllProducts(new SoftFilter(categories, query), page);
        model.addAttribute("softList", soft.getContent());
        // пагинатор
        PaginationHelper.setPaginator(soft, model);
        if (isAjax) {
            // AJAX-запрос - отдаем фрагмент страницы
            return "fragments/soft-list :: softListFragment";
        }
        // Это запрос по прямой ссылке на страницу, отдаем полную страницу
        String url = buildUrl(config.getSoftEndpoint() + path, pageable, query);

        return fullPageHelper.renderFullPage(url, model);
    }

    private String buildUrl(String path, Pageable pageable, String query) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize());

        pageable.getSort().forEach(order ->
                builder.queryParam("sort", order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );

        if (query != null && !query.isBlank()) {
            builder.queryParam("query", query);
        }
        return builder.toUriString();
    }
}

// http://localhost:9000/api/v1/soft/games/all?size=20&page=1
