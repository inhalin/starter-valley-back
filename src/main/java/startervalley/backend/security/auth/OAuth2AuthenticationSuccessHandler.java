package startervalley.backend.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import startervalley.backend.security.jwt.JwtTokenProvider;
import startervalley.backend.utils.CookieUtil;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static startervalley.backend.security.auth.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;

    private final JwtTokenProvider tokenProvider;
    private final CookieAuthorizationRequestRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.info("Response has already been committed");
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // TODO: 환경 설정에 지정된 redirectUri 와 동일한 값인지 검증
//        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
//            throw new BadRequestException("redirect URIs are not matched");
//        }

        String targetUri = redirectUri.orElse(getDefaultTargetUrl());

        String accessToken = tokenProvider.createAccessToken(authentication);
        log.info("accessToken = {}", accessToken);
        tokenProvider.createRefreshToken(authentication, response);

        return UriComponentsBuilder.fromUriString(targetUri)
                .queryParam("isNew", isNewUser(authentication))
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    private Boolean isNewUser(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        return principal.getUser() == null;
    }
}
