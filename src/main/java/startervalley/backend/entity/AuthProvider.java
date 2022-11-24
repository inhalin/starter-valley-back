package startervalley.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    GITHUB("GITHUB", "github", Role.USER),
    GOOGLE("GOOGLE", "google", Role.ADMIN);

    private final String provider;
    private final String name;
    private final Role role;
}
