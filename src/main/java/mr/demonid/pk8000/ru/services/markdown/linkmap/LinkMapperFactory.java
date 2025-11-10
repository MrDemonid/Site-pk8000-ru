package mr.demonid.pk8000.ru.services.markdown.linkmap;


import lombok.AllArgsConstructor;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.dto.ContentType;
import mr.demonid.pk8000.ru.util.AliasPaths;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class LinkMapperFactory {

    private final AliasPaths aliasPaths;
    private final AppConfiguration appConfiguration;


    public LinkMappingStrategy create(ContentType type, String currentPath) {
        return switch (type) {
            case STATIC -> new BaseLinkMapper(aliasPaths.staticUrl(), appConfiguration.getStaticEndpoint(), currentPath);
            case SOFTWARE -> new BaseLinkMapper(aliasPaths.softUrl(), appConfiguration.getSoftEndpoint(), currentPath);
        };
    }

}
