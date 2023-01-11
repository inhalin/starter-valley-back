package startervalley.backend.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CodeGeneratorTest {

    @Test
    void generateRandom() {

        for (int i = 0; i < 100000; i++) {
            String code1 = CodeGenerator.generateRandom();
            String code2 = CodeGenerator.generateRandom();

            assertThat(code1).isNotEqualTo(code2);
        }
    }
}