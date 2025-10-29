package mr.demonid.pk8000.ru.services.markdown;

import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import mr.demonid.pk8000.ru.services.markdown.linkmap.LinkMappingStrategy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;


public class ContentLinkProviderFactory implements AttributeProviderFactory {

    private final LinkMappingStrategy strategy;

    public ContentLinkProviderFactory(LinkMappingStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public @Nullable Set<Class<?>> getAfterDependents() {
        return Set.of();
    }

    @Override
    public @Nullable Set<Class<?>> getBeforeDependents() {
        return Set.of();
    }

    @Override
    public boolean affectsGlobalScope() {
        return false;
    }

    @Override
    public @NotNull AttributeProvider apply(@NotNull LinkResolverContext context) {
        return new ContentLinkProvider(strategy);
    }

}
