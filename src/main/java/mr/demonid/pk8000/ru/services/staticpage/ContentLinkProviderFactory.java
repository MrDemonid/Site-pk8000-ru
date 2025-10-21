package mr.demonid.pk8000.ru.services.staticpage;

import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import mr.demonid.pk8000.ru.configs.AppConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ContentLinkProviderFactory implements AttributeProviderFactory {

    private final String contentPath;
    private final AppConfiguration appConfiguration;

    public ContentLinkProviderFactory(AppConfiguration appConfiguration, String contentPath) {
        this.contentPath = contentPath;
        this.appConfiguration = appConfiguration;
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
        return new ContentLinkProvider(appConfiguration, contentPath);
    }

}
