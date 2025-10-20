package mr.demonid.pk8000.ru.configs;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Достаем роли из токена Keycloak и кладем их в текущий контекст безопасности.
 */
@Log4j2
public class KeycloakOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService delegate = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = delegate.loadUser(userRequest);
        Object rawRoles = oidcUser.getAttribute("roles");
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (rawRoles instanceof List<?> rolesList) {
            authorities = rolesList.stream()
                    .filter(Objects::nonNull)
                    .flatMap(e -> e instanceof String s ? Stream.of(s) : Stream.empty())
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        authorities.addAll(oidcUser.getAuthorities());          // добавляем базовые authority

        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }

}
