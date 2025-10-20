package mr.demonid.pk8000.ru.controller.api;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/admin/v1")
public class AdminController {

    private AdminServiceImpl adminService;


    @PostMapping("/document/add")
    public ResponseEntity<?> addDocument() {
        return ResponseEntity.ok().build();
    }

}
