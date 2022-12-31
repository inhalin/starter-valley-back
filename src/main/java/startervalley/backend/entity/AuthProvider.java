package startervalley.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    GITHUB("GITHUB", "github", Role.USER.getRole());

    private final String provider;
    private final String name;
    private final String role;
}
