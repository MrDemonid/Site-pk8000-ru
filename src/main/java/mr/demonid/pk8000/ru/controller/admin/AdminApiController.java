package mr.demonid.pk8000.ru.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.PageDTO;
import mr.demonid.pk8000.ru.dto.SoftCreateRequest;
import mr.demonid.pk8000.ru.dto.SoftResponse;
import mr.demonid.pk8000.ru.dto.SoftUpdateRequest;
import mr.demonid.pk8000.ru.dto.filters.SoftFilter;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
import mr.demonid.pk8000.ru.services.SoftService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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


    /**
     * Добавление нового или замена существующего изображения.
     * @param productId       Продукт.
     * @param file            Новый файл.
     * @param replaceFileName Имя существующего файла или null.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/upload/image/{productId}")
    public ResponseEntity<?> uploadImage(@PathVariable Long productId,
                                         @RequestPart("file") MultipartFile file,
                                         @RequestParam(value = "replace", required = false) String replaceFileName) {
        adminService.updateImage(productId, file, replaceFileName);
        return ResponseEntity.ok().build();
    }


    /**
     * Удаление изображения.
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/delete/image/{productId}")
    public ResponseEntity<?> deleteImage(@PathVariable Long productId, @RequestParam String fileName) {
        adminService.deleteImage(productId, fileName);
        return ResponseEntity.ok(true);
    }


    /**
     * Добавление нового или замена существующего изображения.
     * @param productId       Продукт.
     * @param file            Новый файл.
     * @param replaceFileName Имя существующего файла или null.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/upload/archive/{productId}")
    public ResponseEntity<?> uploadArchive(@PathVariable Long productId,
                                         @RequestPart("file") MultipartFile file,
                                         @RequestParam(value = "replace", required = false) String replaceFileName) {
        adminService.updateImage(productId, file, replaceFileName);
        return ResponseEntity.ok().build();
    }


    /**
     * Удаление изображения.
     * @param productId Продукт.
     * @param fileName  Имя удаляемого файла
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/delete/arhive/{productId}")
    public ResponseEntity<?> deleteArchive(@PathVariable Long productId, @RequestParam String fileName) {
        adminService.deleteImage(productId, fileName);
        return ResponseEntity.ok(true);
    }


}
