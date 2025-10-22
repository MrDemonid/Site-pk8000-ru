package mr.demonid.pk8000.ru.services.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.exceptions.ErrorCodes;
import mr.demonid.pk8000.ru.exceptions.MenuException;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@Log4j2
public class MenuService {

    private List<MenuItem> menu;
    private final ObjectMapper yamlMapper;

    private final MenuProperties menuProperties;
    private final AppConfiguration appConfiguration;


    public MenuService(MenuProperties properties, AppConfiguration appConfiguration) throws IOException {
        this.menuProperties = properties;
        this.appConfiguration = appConfiguration;
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
    }

    public List<MenuItem> buildMenu() throws IOException {
        List<MenuItem> menu;
        Path root = PathUtil.getRootPath().resolve(appConfiguration.getContentPath());
        try (Stream<Path> files = Files.list(root)) {
            menu = files.toList().stream()
                    .filter(Files::isDirectory)
                    .map(e -> parseFolder(root, e))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(MenuItem::getOrder))
                    .toList();
            return menu;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw  new MenuException(ErrorCodes.BAD_META_YAML_FILE, e.getMessage());
        }
    }

    private MenuItem parseFolder(Path root, Path folder) {
        Path metaPath = folder.resolve(menuProperties.getMetaFile());
        MenuItem item;

        if (!Files.exists(metaPath)) {
            // это служебная папка, не для меню
            return null;
        }
        try (InputStream is = Files.newInputStream(metaPath)) {
            item = yamlMapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            throw  new MenuException(ErrorCodes.BAD_META_YAML_FILE, e.getMessage());
        }
        item.setPath(PathUtil.normalize(root.relativize(folder).normalize().toString(), false));

        log.info("Loaded menu item: title={}, endpoint={}, path={}, isadmin={}", item.getTitle(), item.getEndpoint(), item.getPath(), item.isAdminOnly());

        // рекурсивно добавляем подкаталоги
        try (Stream<Path> files = Files.list(folder)) {
            List<MenuItem> children = files.toList().stream()
                    .filter(Files::isDirectory)
                    .map(e -> parseFolder(root, e))
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(MenuItem::getOrder))
                    .toList();
            if (item.getChildren() == null) {
                item.setChildren(new ArrayList<>());
            }
            item.getChildren().addAll(children);

        } catch (IOException e) {
            throw new MenuException(ErrorCodes.BAD_META_YAML_FILE, e.getMessage());
        }

        return item;
    }


//    /*
//    Присвоение всем пунктам меню уникального идентификатора.
//     */
//    private void assignIds(List<MenuItem> menu) {
//        AtomicLong counter = new AtomicLong(1);
//        menu.forEach(item -> assignIdsRecursive(item, counter));
//    }
//
//    private void assignIdsRecursive(MenuItem item, AtomicLong counter) {
//        if (item.getMenuId() == null) {
//            item.setMenuId("menu-" + counter.getAndIncrement());
//        }
//        if (item.getChildren() != null) {
//            item.getChildren().forEach(child -> assignIdsRecursive(child, counter));
//        }
//    }

}
