package startervalley.backend.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProfileCheckController {

    private final Environment environment;

    public ProfileCheckController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(environment.getActiveProfiles());

        List<String> prodProfiles = Arrays.asList("prod1", "prod2");

        String defaultProfile = profiles.isEmpty() ? "prod1" : prodProfiles.get(0);

        return profiles.stream()
                .filter(prodProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }
}
