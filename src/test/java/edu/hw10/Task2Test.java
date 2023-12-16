package edu.hw10;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hw10.task2.CacheProxy;
import edu.hw10.task2.FibCalculator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class Task2Test {

    @Test
    void cacheProxyTest() throws IOException {
        String path = "src/test/java/edu/hw10/cache.json";
        Files.deleteIfExists(Path.of(path));
        FibCalculator calculator = new FibCalculator() {
            public long fib(int number) {
                return number <= 1 ? number : fib(number - 1) + fib(number - 2);
            }
        };
        FibCalculator proxy = CacheProxy.create(calculator, path);

        assertThat(proxy.fib(10)).isEqualTo(55);
        assertThat(proxy.fib(13)).isEqualTo(233);
        assertThat(proxy.fib(4)).isEqualTo(3);
        assertThat(proxy.fib(45)).isEqualTo(1134903170);
        assertThat(proxy.fib(47)).isEqualTo(2971215073L);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Long> map = mapper.readValue(
            new File(path),
            new TypeReference<ConcurrentHashMap<String, Long>>() {
            }
        );
        assertThat(map).containsEntry("fib_4", 3L);
        assertThat(map).containsEntry("fib_13", 233L);
        assertThat(map).containsEntry("fib_10", 55L);
        assertThat(map).containsEntry("fib_45", 1134903170L);
        assertThat(map).containsEntry("fib_47", 2971215073L);
    }
}
