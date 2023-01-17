package startervalley.backend.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
    GITHUB("GITHUB", "github");

    private final String provider;
    private final String name;
}
