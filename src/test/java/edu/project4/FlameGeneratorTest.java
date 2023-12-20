package edu.project4;

import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FlameGeneratorTest {

    @Test
    @DisplayName("Тест функции создания изображения")
    void rendererTest() {
        ConfigParser configParser = new ConfigParser(Path.of("src/test/java/edu/project4/test-config.txt"));
        MultiThreadRenderer renderer = new MultiThreadRenderer(configParser.height, configParser.width);
        renderer.generateImage(configParser.iterations, configParser.threshold, configParser.transformations);
        assertThat(renderer.image.length).isEqualTo(configParser.height);
        assertThat(renderer.image[0].length).isEqualTo(configParser.width);
        assertThat(renderer.image[0][0]).isNotNull();
    }

    @Test
    @DisplayName("Тестирование считывания конфига")
    void configParserTest() {
        ConfigParser configParser = new ConfigParser(Path.of("src/test/java/edu/project4/test-config.txt"));
        assertThat(configParser.format).isEqualTo("jpeg");
        assertThat(configParser.iterations).isEqualTo(50000000);
        assertThat(configParser.window).isEqualTo(3);
        assertThat(configParser.width).isEqualTo(3240);
    }

    @Test
    @DisplayName("Тестирование считывания из неправильного файла")
    void configParserWithWrongFileConfigTest() {
        final Path configPath = Path.of("src/test/java/edu/project4/badConfig.txt");
        assertThrows(IllegalArgumentException.class, () -> new ConfigParser(configPath));
    }
}
