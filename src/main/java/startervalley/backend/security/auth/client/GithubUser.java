package startervalley.backend.security.auth.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import startervalley.backend.entity.AuthProvider;
import startervalley.backend.entity.Role;
import startervalley.backend.entity.User;

@Getter
@AllArgsConstructor
public class GithubUser extends User {
    private final String username;
    private final Role role;
    private final AuthProvider provider;
}
