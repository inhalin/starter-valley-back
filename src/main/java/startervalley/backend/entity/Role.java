package startervalley.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN", "admin"),
    USER("ROLE_USER", "user"),
    ANONYMOUS("ROLE_ANONYMOUS", "anonymous");

    private final String role;
    private final String name;
}
