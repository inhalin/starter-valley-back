package startervalley.backend.security.auth.client;

import startervalley.backend.dto.auth.GithubUserResponse;

public interface ClientProxy {

    String getAccessToken(String code);

    GithubUserResponse getUserData(String accessToken);
}
