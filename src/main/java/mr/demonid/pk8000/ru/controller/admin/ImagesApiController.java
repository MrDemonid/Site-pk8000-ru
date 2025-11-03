package mr.demonid.pk8000.ru.controller.admin;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.ImageResponse;
import mr.demonid.pk8000.ru.services.admin.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/v1/admin")
public class ImagesApiController {

    private ImageService imageService;


    /**
     * Возвращает список изображений.
     *
     * @param productId Продукт.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/images-manage/{productId}")
    public ResponseEntity<List<ImageResponse>> getListImages(@PathVariable Long productId) {
        return ResponseEntity.ok(imageService.getFiles(productId));
    }


    /**
     * Добавление нового изображения.
     *
     * @param productId Продукт.
     * @param file      Новый файл.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/images-manage/{productId}")
    public ResponseEntity<Void> uploadImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        imageService.updateFile(productId, file, null);
        return ResponseEntity.ok().build();
    }


    /**
     * Замена существующего изображения.
     *
     * @param productId Продукт.
     * @param file      Новый файл.
     * @param imageName Имя существующего файла или null.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PutMapping("/images-manage/{productId}/{imageName}")
    public ResponseEntity<Void> replaceImage(@PathVariable Long productId, @PathVariable String imageName, @RequestParam("file") MultipartFile file) {
        imageService.updateFile(productId, file, imageName);
        return ResponseEntity.ok().build();
    }


    /**
     * Удаление изображения.
     *
     * @param productId Продукт.
     * @param imageName Имя удаляемого файла
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/images-manage/{productId}/{imageName}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long productId, @PathVariable String imageName) {
        imageService.deleteFile(productId, imageName);
        return ResponseEntity.ok().build();
    }


}
