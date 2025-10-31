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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/v1/admin")
public class AdminController {

    private SoftService softService;
    private final FullPageHelper fullPageHelper;
    private final MenuProperties menuProperties;


    @GetMapping("/soft")
    String doSoft(@PageableDefault(size = 12, page = 0, direction = Sort.Direction.ASC) Pageable pageable,
                  Model model,
                  @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        // делаем выборку из БД
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "name"));
        Page<SoftResponse> soft = softService.getAllProducts(new SoftFilter(), page);

        model.addAttribute("menu", menuProperties);
        model.addAttribute("softList", soft.getContent());
        model.addAttribute("categories", CategoryType.values());
        PaginationHelper.setPaginator(soft, model);

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX-запрос - отдаем фрагмент страницы
            return "fragments/soft-list-edit :: softListEdit";
        }
        // Это запрос по прямой ссылке на страницу (или её обновление), отдаем полную страницу
        String url = buildUrl("/api/v1/admin/soft", pageable);
        return fullPageHelper.renderFullPage(url, model);
    }

    private String buildUrl(String path, Pageable pageable) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(path)
                .queryParam("page", pageable.getPageNumber())
                .queryParam("size", pageable.getPageSize());

        pageable.getSort().forEach(order ->
                builder.queryParam("sort", order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );

        return builder.toUriString();
    }


}
