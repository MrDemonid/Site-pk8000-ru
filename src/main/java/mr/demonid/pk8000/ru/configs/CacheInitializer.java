package mr.demonid.pk8000.ru.configs;

import lombok.RequiredArgsConstructor;
import mr.demonid.pk8000.ru.services.DescriptionCacheRefreshService;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final DescriptionCacheRefreshService refreshService;

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {
        refreshService.refreshAll();
    }
}
