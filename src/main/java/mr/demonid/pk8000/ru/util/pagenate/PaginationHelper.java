package mr.demonid.pk8000.ru.util.pagenate;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;


public class PaginationHelper {

    private static final int DEFAULT_PAGE_SIZE = 7;

    /**
     * Задает параметры для кнопок переходов по страницам.
     */
    public static void setPaginator(Page<?> page, Model model) {
        List<PageButton> pageButtons = getVisiblePageNumbers(page, DEFAULT_PAGE_SIZE);
        model.addAttribute("pageButtons", pageButtons);
        model.addAttribute("pageData", page);
    }


    private static List<PageButton> getVisiblePageNumbers(Page<?> pageData, int windowSize) {
        int totalPages = pageData.getTotalPages();
        int currentPage = pageData.getNumber();

        if (totalPages <= windowSize) {
            List<PageButton> buttons = new ArrayList<>();
            for (int i = 0; i < totalPages; i++) {
                buttons.add(new PageButton(i, String.valueOf(i + 1), i == currentPage, false));
            }
            return buttons;
        }

        List<PageButton> buttons = new ArrayList<>();

        boolean leftEllipsis = currentPage > 2;
        boolean rightEllipsis = currentPage < totalPages - 3;

        int ellipsisCount = 0;
        if (leftEllipsis) ellipsisCount++;
        if (rightEllipsis) ellipsisCount++;

        int visibleNumbers = windowSize - ellipsisCount;

        int start = Math.max(1, currentPage - visibleNumbers / 2);
        int end = start + visibleNumbers - 1;

        if (end >= totalPages - 1) {
            end = totalPages - 2;
            start = end - visibleNumbers + 1;
        }

        // Первая страница всегда
        buttons.add(new PageButton(0, "1", currentPage == 0, false));

        if (leftEllipsis) {
            buttons.add(new PageButton(null, "...", false, true));
        }

        for (int i = start; i <= end; i++) {
            buttons.add(new PageButton(i, String.valueOf(i + 1), i == currentPage, false));
        }

        if (rightEllipsis) {
            buttons.add(new PageButton(null, "...", false, true));
        }

        // Последняя страница всегда
        buttons.add(new PageButton(totalPages - 1, String.valueOf(totalPages), currentPage == totalPages - 1, false));

        return buttons;
    }

}
