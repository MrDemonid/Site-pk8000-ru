package mr.demonid.pk8000.ru.services.markdown;


import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.emoji.EmojiExtension;
import com.vladsch.flexmark.ext.emoji.EmojiImageType;
import com.vladsch.flexmark.ext.emoji.EmojiShortcutType;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.parser.ParserEmulationProfile;
import com.vladsch.flexmark.util.data.MutableDataSet;
import mr.demonid.pk8000.ru.dto.ContentType;
import mr.demonid.pk8000.ru.services.markdown.linkmap.LinkMapperFactory;
import mr.demonid.pk8000.ru.services.markdown.linkmap.LinkMappingStrategy;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MarkdownService {

    private final Parser parser;
    private final MutableDataSet baseOptions;

    private final LinkMapperFactory linkMapperFactory;


    public MarkdownService(LinkMapperFactory linkMapperFactory) {
        this.linkMapperFactory = linkMapperFactory;

        baseOptions = new MutableDataSet()
                .set(Parser.EXTENSIONS, Arrays.asList(
//                        AutolinkExtension.create(),         // превращает URL в ссылку (<a ...>)
                        EmojiExtension.create(),            // поддержка эмодзи
                        StrikethroughExtension.create(),    // зачеркивание текста
                        TaskListExtension.create(),         // поддержка checkbox ([x]... [ ]...)
                        TablesExtension.create(),           // поддержка таблиц
                        TypographicExtension.create(),       // "умные" символы
                        AttributesExtension.create()
                ))
                // set GitHub table parsing options
                .set(TablesExtension.WITH_CAPTION, true)    // таблица с подписью
                .set(TablesExtension.COLUMN_SPANS, false)   // отключены объединенные колонки
                .set(TablesExtension.MIN_HEADER_ROWS, 0)    // кол-во строк в заголовке
                .set(TablesExtension.MAX_HEADER_ROWS, 1)
                .set(TablesExtension.APPEND_MISSING_COLUMNS, true)  // добавлять пустые колонки (по необходимости)
                .set(TablesExtension.DISCARD_EXTRA_COLUMNS, true)   // лишние колонки отбрасываются
                .set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true)
                //.set(EmojiExtension.ROOT_IMAGE_PATH, emojiInstallDirectory())
                .set(EmojiExtension.USE_SHORTCUT_TYPE, EmojiShortcutType.GITHUB)    // поддерживаем синтаксис GitHub :smile:
                .set(EmojiExtension.USE_IMAGE_TYPE, EmojiImageType.IMAGE_ONLY)      // рендерятся как картинки, вместо unicode-символов
                .set(Parser.LISTS_ORDERED_LIST_MANUAL_START, true); // нумерованные списки с любого числа

        baseOptions.setFrom(ParserEmulationProfile.MARKDOWN);
        baseOptions.set(HtmlRenderer.GENERATE_HEADER_ID, false);    // не генерируем якоря для заголовков

        parser = Parser.builder(baseOptions).build();
    }

    public String toHtmlStatic(String markdown, String contentPath) {
        return  render(markdown, linkMapperFactory.create(ContentType.STATIC, contentPath));
    }

    public String toHtmlSoft(String markdown, String contentPath) {
        return render(markdown, linkMapperFactory.create(ContentType.SOFTWARE, contentPath));
    }

    /** Общий метод рендера с выбранной стратегией ссылок */
    private String render(String markdown, LinkMappingStrategy strategy) {
        HtmlRenderer renderer = HtmlRenderer.builder(baseOptions)
                .attributeProviderFactory(new ContentLinkProviderFactory(strategy))
                .nodeRendererFactory(context -> new SignalNodeRenderer())
                .build();

        return renderer.render(parser.parse(markdown));
    }
}
