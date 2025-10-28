package mr.demonid.pk8000.ru.controller.api;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.SoftService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/soft")
@Log4j2
public class SoftApiController {

    private SoftService softService;


    /**
     * Точка входа для чтения тела описания продукта.
     * Сделана отдельно, чтобы не грузить без нужды.
     */
    @GetMapping("/games/description/{productId}")
    public ResponseEntity<String> getSoftDescription(@PathVariable Long productId) {
        String description = softService.getDescription(productId);
        if (description == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(description);
    }

}
