package mr.demonid.pk8000.ru.controller.api;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Log4j2
public class IndexPage {

    @GetMapping("/user")
    public String index(@AuthenticationPrincipal OidcUser oidcUser) {
        if (oidcUser == null) {
            return "user is null";
        }
        String name = oidcUser.getFullName(); // или oidcUser.getName()
        String email = oidcUser.getEmail();
        String subject = oidcUser.getSubject(); // sub
        Map<String, Object> claims = oidcUser.getClaims();
        log.info("------------------------");
        log.info("Имя: {}, email: {}, subject: {}", name, email, subject);
        log.info("Claims: {}", claims);
        log.info("Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        log.info("Authorities: {}", oidcUser.getAuthorities());
        log.info("------------------------");
        List<String> auths = getCurrentUserAuthorities();
        log.info("Current user authorities: {}", auths);
        return "Пользователь: " + name + ", email: " + email + ", subject: " + subject + ", claims: " + claims + "auth: " + oidcUser.getAuthorities();
    }

    /**
     * Возвращает список прав текущего пользователя.
     */
    public static List<String> getCurrentUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of();
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    @GetMapping("/token")
    public String token() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();

        if (auth instanceof OAuth2AuthenticationToken oauth2Token) {
            OidcUser user = (OidcUser) oauth2Token.getPrincipal();
            String name = user.getFullName();
            String idToken = user.getIdToken().getTokenValue(); // << access token!
            log.info("Name: {}, Token: {}", name, idToken);
            return "Name: " + name + ", IdToken: " + idToken;
        }
        return "No Authentication!";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin";
    }

}
