package startervalley.backend.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtTokenProvider tokenProvider;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        String token = tokenProvider.parseBearerToken(request);

        if (StringUtils.hasText(token)) {
            tokenProvider.removeExpiredToken(token);
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getLocalizedMessage());
    }
}
