package mr.demonid.pk8000.ru.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.PageDTO;
import mr.demonid.pk8000.ru.dto.SoftCreateRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.SoftUpdateRequest;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.services.SoftService;
import mr.demonid.pk8000.ru.services.admin.AdminServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/v1/admin")
public class AdminApiController {

    private AdminServiceImpl adminService;
    private SoftService softService;


    /**
     * Возвращает постраничный список товаров, включая и тех, которых нет в наличии.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/read")
    public ResponseEntity<PageDTO<SoftResponse>> getAllProducts(@RequestBody SoftFilter filter, Pageable pageable) {
        return ResponseEntity.ok(new PageDTO<>(softService.getAllProducts(filter, pageable)));
    }


    /**
     * Создание нового продукта.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody SoftCreateRequest product) {
        adminService.createProduct(product);
        return ResponseEntity.ok().build();
    }


    /**
     * Обновление данных о продукте.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody SoftUpdateRequest product) {
        adminService.updateProduct(product);
        return ResponseEntity.ok().build();
    }


    /**
     * Удаление продукта.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/delete/product/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        adminService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
