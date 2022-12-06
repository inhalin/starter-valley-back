package startervalley.backend.security.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import startervalley.backend.dto.auth.GithubEmailResponse;
import startervalley.backend.dto.auth.GithubTokenResponse;
import startervalley.backend.dto.auth.GithubUserResponse;
import startervalley.backend.entity.AuthProvider;
import startervalley.backend.exception.TokenNotValidException;

@Component
@RequiredArgsConstructor
public class ClientGithub implements ClientProxy {

    private final WebClient webClient;

    @Value("${app.oauth2.client.github.access-token-uri}")
    private String accessTokenUri;

    @Value("${app.oauth2.client.github.client-id}")
    private String clientId;

    @Value("${app.oauth2.client.github.client-secret}")
    private String clientSecret;

    public String getAccessToken(String code) {

        String uri = UriComponentsBuilder.fromUriString(accessTokenUri)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("code", code)
                .toUriString();

        GithubTokenResponse tokenResponse = webClient.post().uri(uri)
                .header("Accept", "application/json")
                .retrieve()
                .bodyToMono(GithubTokenResponse.class)
                .block();

        return tokenResponse.getAccessToken();
    }

    @Override
    public GithubUserResponse getUserData(String accessToken) {
        GithubUserResponse githubUserResponse = webClient.get()
                .uri("https://api.github.com/user")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new TokenNotValidException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenNotValidException("Internal Server Error")))
                .bodyToMono(GithubUserResponse.class)
                .block();

        if (githubUserResponse.getEmail() == null) {
            githubUserResponse.setEmail(fetchEmail(accessToken));
        }

        githubUserResponse.setProvider(AuthProvider.GITHUB);
        return githubUserResponse;
    }

    private String fetchEmail(String accessToken) {
        GithubEmailResponse[] emailResponses = webClient.get()
                .uri("https://api.github.com/user/emails")
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/vnd.github+json")
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new TokenNotValidException("Social Access Token is unauthorized")))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new TokenNotValidException("Internal Server Error")))
                .bodyToMono(GithubEmailResponse[].class)
                .block();

        if (emailResponses.length > 0) {
            return emailResponses[0].getEmail();
        }

        return null;
    }
}
