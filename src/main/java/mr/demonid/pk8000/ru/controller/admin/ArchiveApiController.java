package mr.demonid.pk8000.ru.controller.admin;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.ArchiveResponse;
import mr.demonid.pk8000.ru.services.admin.ArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/v1/admin")
public class ArchiveApiController {

    private final ArchiveService archiveService;


    /**
     * Возвращает список файлов.
     *
     * @param productId Продукт.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/archives-manage/{productId}")
    public ResponseEntity<List<ArchiveResponse>> getListArchives(@PathVariable Long productId) {
        return ResponseEntity.ok(archiveService.getArchives(productId));
    }


    /**
     * Добавление нового файла.
     *
     * @param productId Продукт.
     * @param file      Новый файл.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/archives-manage/{productId}")
    public ResponseEntity<Void> uploadArchive(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        archiveService.updateArchive(productId, file, null);
        return ResponseEntity.ok().build();
    }


    /**
     * Замена существующего файла.
     *
     * @param productId Продукт.
     * @param file      Новый файл.
     * @param imageName Имя существующего файла или null.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PutMapping("/archives-manage/{productId}/{imageName}")
    public ResponseEntity<Void> replaceArchive(@PathVariable Long productId, @PathVariable String imageName, @RequestParam("file") MultipartFile file) {
        archiveService.updateArchive(productId, file, imageName);
        return ResponseEntity.ok().build();
    }


    /**
     * Удаление файла.
     *
     * @param productId Продукт.
     * @param imageName Имя удаляемого файла
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/archives-manage/{productId}/{imageName}")
    public ResponseEntity<Void> deleteArchive(@PathVariable Long productId, @PathVariable String imageName) {
        archiveService.deleteArchive(productId, imageName);
        return ResponseEntity.ok().build();
    }

}
