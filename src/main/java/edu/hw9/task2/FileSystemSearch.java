package edu.hw9.task2;

import java.io.File;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class FileSystemSearch {
    private FileSystemSearch() {
    }

    public static List<File> searchDirectoriesWithMoreThanCountFiles(String path, int countFiles) {
        try (ForkJoinPool pool = new ForkJoinPool();) {
            File rootDirectory = new File(path);
            return pool.invoke(new FileCountTask(rootDirectory, countFiles));
        }
    }

    public static List<File> searchFilesWithASpecificExtensionAndSize(
        String path,
        String extension,
        long sizeThreshold
    ) {
        try (ForkJoinPool pool = new ForkJoinPool();) {
            File rootDirectory = new File(path);
            return pool.invoke(new FileSearchTask(rootDirectory, extension, sizeThreshold));
        }
    }
}
