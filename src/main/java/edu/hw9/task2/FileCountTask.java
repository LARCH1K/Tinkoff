package edu.hw9.task2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FileCountTask extends RecursiveTask<List<File>> {
    private final File directory;

    private final int countFiles;

    FileCountTask(File directory, final int countFiles) {
        this.directory = directory;
        this.countFiles = countFiles;
    }

    @Override
    protected List<File> compute() {
        File[] files = directory.listFiles();
        List<FileCountTask> subtasks = new ArrayList<>();
        List<File> result = new ArrayList<>();
        if (files != null) {
            if (files.length > countFiles) {
                result.add(directory);
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    FileCountTask subtask = new FileCountTask(file, countFiles);
                    subtasks.add(subtask);
                    subtask.fork();
                }
            }
        }
        for (FileCountTask subtask : subtasks) {
            result.addAll(subtask.join());
        }
        return result;
    }
}
