package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class AppStartupListener {

    private AdminServiceImpl adminService;


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("🚀 Приложение полностью готово к работе!");
    }

}
