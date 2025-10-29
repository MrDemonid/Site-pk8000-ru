package mr.demonid.pk8000.ru.services.markdown;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.html.MutableAttributes;
import com.vladsch.flexmark.util.sequence.BasedSequence;
import mr.demonid.pk8000.ru.services.markdown.linkmap.LinkMappingStrategy;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


/**
 * Провайдер обработки ссылок в markdown, при их переводе в HTML.
 */
public class ContentLinkProvider implements AttributeProvider {

    private final LinkMappingStrategy strategy;
    private Map<BasedSequence, BasedSequence> refs = new HashMap<>();


    public ContentLinkProvider(LinkMappingStrategy strategy) {
        this.strategy = strategy;
    }


    @Override
    public void setAttributes(@NotNull Node node, @NotNull AttributablePart attributablePart, @NotNull MutableAttributes attributes) {

        if (node instanceof Reference ref) {
            refs.put(ref.getReference(), ref.getUrl()); // следом пойдет сама ссылка (ImageRef или LinkRef)
            return;
        }

        if (node instanceof ImageRef ref) {
            // ссылка на рисунок, вида: ![название][ссылка] ... [ссылка]: <путь>
            if (refs.containsKey(ref.getReference())) {
                attributes.replaceValue("src", strategy.mapLink(refs.get(ref.getReference()).toString()));
            }
        }

        if (node instanceof LinkRef ref) {
            // Косвенная ссылка на ресурс: [Схема][original_scheme] ... [original_scheme]: ./files/hdd-vv55-rev-1_0.zip
            if (refs.containsKey(ref.getReference())) {
                attributes.replaceValue("href", strategy.mapLink(refs.get(ref.getReference()).toString()));
            }
        }

        if (node instanceof Image || node instanceof Link) {
            String attr = node instanceof Image ? "src" : "href";
            String value = attributes.getValue(attr);
            if (value == null || value.startsWith("http") || value.startsWith("/"))
                return;
            attributes.replaceValue(attr, strategy.mapLink(value));
        }

    }

}
