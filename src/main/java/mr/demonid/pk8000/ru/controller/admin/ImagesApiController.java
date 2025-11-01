package mr.demonid.pk8000.ru.controller.admin;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.dto.ImageResponse;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
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

    private AdminServiceImpl adminService;


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @GetMapping("/images-manage/{productId}")
    public ResponseEntity<?> loadImages(@PathVariable Long productId) {
        System.out.println("Load images for " + productId);
        List<ImageResponse> l = adminService.getImages(productId);
        System.out.println("get images: " + l);
        return ResponseEntity.ok(adminService.getImages(productId));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PostMapping("/images-manage/{productId}")
    public ResponseEntity<?> uploadImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file) {
        adminService.updateImage(productId, file, null);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'DEVELOPER')")
    @PutMapping("/images-manage/{productId}/{imageName}")
    public ResponseEntity<?> replaceImage(@PathVariable Long productId, @PathVariable String imageName, @RequestParam("file") MultipartFile file) {
        System.out.println("replaceImage: " + imageName);
        adminService.updateImage(productId, file, imageName);
        return ResponseEntity.ok().build();
    }

}
