package mr.demonid.pk8000.ru.services.staticpage;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.MutableAttributes;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import mr.demonid.pk8000.ru.util.PathUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


/**
 * Дополнительная обработка ссылок в MARKDOWN, при преобразовании в HTML.
 */
public class ContentLinkProvider implements AttributeProvider {
    private final String contentPath;
    private final AppConfiguration config;

    Map<BasedSequence, BasedSequence> refs = new HashMap<>();


    public ContentLinkProvider(AppConfiguration appConfiguration, String contentPath) {
        this.contentPath = contentPath;
        this.config = appConfiguration;
    }

    @Override
    public void setAttributes(@NotNull Node node, @NotNull AttributablePart part, @NotNull MutableAttributes attributes) {
        if (node instanceof Reference ref) {
            refs.put(ref.getReference(), ref.getUrl()); // следом пойдет сама ссылка (ImageRef или LinkRef)
            return;
        }

        if (node instanceof ImageRef) {
            // ссылка на рисунок, вида: ![название][ссылка] ... [ссылка]: <путь>
            if (refs.containsKey(((ImageRef) node).getReference())) {
                setAttache(attributes, refs.get(((ImageRef) node).getReference()).toString(), "src");
            }
        }

        if (node instanceof LinkRef) {
            // Косвенная ссылка на ресурс: [Схема][original_scheme] ... [original_scheme]: ./files/hdd-vv55-rev-1_0.zip
            if (refs.containsKey(((LinkRef) node).getReference())) {
                setAttache(attributes, refs.get(((LinkRef) node).getReference()).toString(), "href");
            }
        }

        if (node instanceof Image || node instanceof Link) {
            String attr = node instanceof Image ? "src" : "href";
            String value = attributes.getValue(attr);
            if (value == null || value.startsWith("http") || value.startsWith("/"))
                return;

            if (value.endsWith(".md")) {
                // это ссылка на другую страницу, направляем её на эндпоинт для статичных страниц.
                // вычисляем относительный путь от текущей страницы
                Path base = Paths.get(contentPath);
                String resolvedStr = PathUtil.normalize(base.resolve(value).normalize().toString(), true);

                // удаляем имя md-файла из пути
                resolvedStr = resolvedStr.replaceFirst("/?[^/]+\\.md$", "");
                attributes.replaceValue(attr, config.getStaticEndpoint() + resolvedStr);
            } else {
                // прямая ссылка на ресурс: [Схема](./files/hdd-vv55-rev-1_0.zip)
                setAttache(attributes, value, attr);
            }
        }
    }

    private void setAttache(MutableAttributes attributes, String url, String dest) {
        Path path = Path.of(
                        config.getAttacheUrl().replaceFirst("^/", ""),
                        contentPath.replaceFirst("^/", ""),
                        url.replaceFirst("^/", ""))
                .normalize();
        attributes.replaceValue(dest, PathUtil.normalize(File.separator + path, true));
    }
}
