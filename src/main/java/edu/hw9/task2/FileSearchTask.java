package edu.hw9.task2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FileSearchTask extends RecursiveTask<List<File>> {
    private final File directory;
    private final String extension;
    private final long sizeThreshold;

    FileSearchTask(File directory, String extension, long sizeThreshold) {
        this.directory = directory;
        this.extension = extension;
        this.sizeThreshold = sizeThreshold;
    }

    @Override
    protected List<File> compute() {
        File[] files = directory.listFiles();
        List<FileSearchTask> subtasks = new ArrayList<>();
        List<File> result = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileSearchTask subtask = new FileSearchTask(file, extension, sizeThreshold);
                    subtasks.add(subtask);
                    subtask.fork();
                } else {
                    if ((extension == null || file.getName().endsWith(extension))
                        && file.length() > sizeThreshold) {
                        result.add(file);
                    }
                }
            }
        }
        for (FileSearchTask subtask : subtasks) {
            result.addAll(subtask.join());
        }
        return result;
    }
}
