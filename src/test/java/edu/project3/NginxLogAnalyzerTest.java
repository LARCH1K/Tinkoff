package edu.project3;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NginxLogAnalyzerTest {

    @Test
    void nginxLogAnalyzerTest() throws IOException {
        NginxLogAnalyzer nginxLogAnalyzer = new NginxLogAnalyzer();

        nginxLogAnalyzer.analyzeLogs(new String[]{"--path", "src/main/resources/project3/testPr3/dirPr3/2045.txt"
            , "--from", "2023-08-31", "--format", "markdown"});
        Path pathExpected = Path.of("src/main/resources/project3/testPr3/RightResult.md");
        Path pathActual = Path.of("src/main/resources/project3/result.md");
        assertEquals(Files.readAllLines(pathExpected), Files.readAllLines(pathActual));

        nginxLogAnalyzer.analyzeLogs(new String[]{"--path", "src/main/resources/project3/testPr3/dirPr3/2045.txt"
            , "--from", "2023-08-31", "--format", "adoc"});
        pathExpected = Path.of("src/main/resources/project3/testPr3/RightResult.adoc");
        pathActual = Path.of("src/main/resources/project3/result.adoc");
        assertEquals(Files.readAllLines(pathExpected), Files.readAllLines(pathActual));
    }
}
