package mr.demonid.pk8000.ru.services.staticpage;

import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;

import java.util.Set;

/**
 * Подмена текста MARKDOWN на спец стили.
 * Например: /XXX будет отображаться как активный сигнал низкого уровня (т.е. текст с чертой сверху).
 */
public class SignalNodeRenderer implements NodeRenderer {

    @Override
    public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
        return Set.of(
            new NodeRenderingHandler<>(Text.class, this::renderText)
        );
    }

    /**
     * Заменяет /TEXT на TEXT с надчеркиванием, для обозначения активного низкого уровня сигнала.
     */
    private void renderText(Text node, NodeRendererContext context, HtmlWriter html) {
        String text = node.getChars().toString();
        text = text.replaceAll("(?<![A-Za-z0-9/])/(?:([A-Z0-9]{2,6}))(?![A-Za-z0-9/])",
                "<span class=\"signal-low\">$1</span>");

        html.raw(text);
    }
}
