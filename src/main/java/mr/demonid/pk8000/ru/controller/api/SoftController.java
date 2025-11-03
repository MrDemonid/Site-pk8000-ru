package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.domain.CategoryGroup;
import mr.demonid.pk8000.ru.domain.CategoryType;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.ServiceException;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/api/v1/soft")
@Log4j2
public class SoftController {

    private final SoftService softService;
    private final MenuProperties menuProperties;
    private final FullPageHelper fullPageHelper;
    private final AppConfiguration config;

    // карта соответствия эндпоинтов и категорий
    private static final Map<String, List<CategoryType>> mapCategories;


    static {
        mapCategories = new HashMap<>();
        mapCategories.put("/games/all", Arrays.stream(CategoryType.values()).filter(c -> c.getGroup() == CategoryGroup.GAMES).toList());
        mapCategories.put("/games/arcade", List.of(CategoryType.ARCADE));
        mapCategories.put("/games/car", List.of(CategoryType.RACING));
        mapCategories.put("/games/sport", List.of(CategoryType.SPORT));
        mapCategories.put("/games/logic", List.of(CategoryType.LOGIC));
        mapCategories.put("/games/edu", List.of(CategoryType.EDU));
        mapCategories.put("/games/other", List.of(CategoryType.OTHER_GAMES));
        mapCategories.put("/all", Arrays.stream(CategoryType.values()).filter(c -> c.getGroup() == CategoryGroup.SOFTWARE).toList());
        mapCategories.put("/tools", List.of(CategoryType.TOOLS));
        mapCategories.put("/system", List.of(CategoryType.SYSTEM));
        mapCategories.put("/code", List.of(CategoryType.PROGRAMMING));
        mapCategories.put("/other", List.of(CategoryType.OTHER_SOFTWARE));
    }


    /**
     * Точка входа для контента типа "Софт".
     * В зависимости от типа запроса возвращает либо фрагмент страницы,
     * либо полную страницу вместе с фрагментом.
     * @param path          Путь до категории.
     * @param pageable      Размер страниц и номер текущей.
     * @param query         Поисковый запрос.
     * @param requestedWith Флаг Ajax-запроса.
     * @param model         Она и в Африке модель...
     */
    @GetMapping("/{*path}")
    public String soft(@PathVariable("path") String path,
                       @PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
                       @RequestParam(defaultValue = "") String query,
                       @RequestHeader(value = "X-Requested-With", required = false) String requestedWith,
                       Model model) {

        // получаем список ID категорий
        List<CategoryType> categories = mapCategories.get(path);
        if (categories == null) {
            throw new ServiceException(ErrorCodes.BAD_SOFT_CATEGORY, "Category not found");
        }
        List<Long> catIds = categories.stream().map(menuProperties::getCategoryId).toList();

        // делаем выборку из БД
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
        Page<SoftResponse> soft = softService.getAllProducts(new SoftFilter(catIds, query), page);

        model.addAttribute("softList", soft.getContent());
        PaginationHelper.setPaginator(soft, model);

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX-запрос - отдаем фрагмент страницы
            return "fragments/soft-list :: softListFragment";
        }
        // Это запрос по прямой ссылке на страницу, отдаем полную страницу
        String url = buildUrl(config.getSoftEndpoint() + path, pageable, query);

        return fullPageHelper.renderFullPage(url, model);
    }


    /**
     * Формирует полный URL, включающий параметры размера страниц, текущей страницы и query.
     */
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

