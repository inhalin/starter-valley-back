package startervalley.backend.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CodeGenerator {

    public static String generateRandom() {
        return "wjtb-" + UUID.randomUUID().toString().split("-")[0];
    }
}
