package edu.hw9;

import edu.hw9.task2.FileSystemSearch;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task2Test {
    @Test
    void searchDirectoriesWithMoreThan1000FilesTest() throws IOException {
        Path tempDir = Files.createTempDirectory("testSearchDirectoriesWithMoreThan1000Files");
        createDirectoriesWithMoreThan1000Files(tempDir, 3, 1001);

        List<File> result = FileSystemSearch.searchDirectoriesWithMoreThanCountFiles(tempDir.toString(), 1000);

        assertEquals(3, result.size());
    }

    @Test
    void searchFilesWithASpecificExtensionAndSizeTest() throws IOException {
        Path tempDir = Files.createTempDirectory("testSearchFilesWithASpecificExtensionAndSize");
        createFilesWithExtensionAndSize(tempDir, "txt", 1024, 3);

        List<File> result = FileSystemSearch.searchFilesWithASpecificExtensionAndSize(tempDir.toString(), "txt", 1000);

        assertEquals(3, result.size());
    }

    private void createDirectoriesWithMoreThan1000Files(Path baseDir, int numDirs, int filesPerDir) throws IOException {
        for (int i = 0; i < numDirs; i++) {
            Path dir = baseDir.resolve("dir" + i);
            Files.createDirectory(dir);
            for (int j = 0; j < filesPerDir; j++) {
                Files.createFile(dir.resolve("file" + j));
            }
        }
    }

    private void createFilesWithExtensionAndSize(Path baseDir, String extension, long size, int numFiles) throws IOException {
        for (int i = 0; i < numFiles; i++) {
            Files.createFile(baseDir.resolve("file" + i + "." + extension));
            Files.write(baseDir.resolve("file" + i + "." + extension), new byte[(int) size]);
        }
    }
}
