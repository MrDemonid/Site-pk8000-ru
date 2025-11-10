package mr.demonid.pk8000.ru.controller.admin;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.DescriptionMetaRequest;
import mr.demonid.pk8000.ru.dto.DescriptionResponse;
import mr.demonid.pk8000.ru.services.admin.DescriptionService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/v1/admin")
public class DescriptionApiController {


    private final DescriptionService descriptionService;


    /**
     * Возвращает описание продукта.
     *
     * @param productId Продукт.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/description-manage/{productId}")
    public ResponseEntity<DescriptionResponse> getDescription(@PathVariable Long productId) {

        return ResponseEntity.ok(descriptionService.getDescription(productId));
    }


    /**
     * Обновление описания продукта.
     * @param productId       Продукт.
     * @param meta            Информация об удаляемых/изменяемых файлах.
     * @param attachments     Список добавляемых файлов.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PutMapping(value = "/description-manage/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DescriptionResponse> updateDescription(
            @PathVariable Long productId,
            @RequestPart(value = "meta", required = false) DescriptionMetaRequest meta,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {

        return ResponseEntity.ok(descriptionService.updateDescription(productId, attachments, meta));
    }


    /**
     * Архивирует описание и отправляет клиенту.
     * @param productId Продукт.
     * @return zip-файл.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/description-manage/zip/{productId}")
    public ResponseEntity<byte[]> downloadDescriptionAsZip(@PathVariable Long productId) {

        byte[] zip = descriptionService.archiveProductDescription(productId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("description_" + productId + ".zip")
                .build());
        headers.setContentLength(zip.length);

        return new ResponseEntity<>(zip, headers, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @DeleteMapping("/description-manage/{productId}")
    public ResponseEntity<Void> deleteDescription(@PathVariable Long productId) {

        descriptionService.deleteDescription(productId);
        return ResponseEntity.ok().build();
    }


}
