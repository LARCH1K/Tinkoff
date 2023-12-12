package edu.project3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class LogFileReader {
    public List<String> readLogs(String path) {

        List<String> res = new ArrayList<>();
        if (!path.contains("*")) {
            String validPath = String.valueOf(getValidPath(path));
            getLogsFromFile(validPath, res);
        } else {
            getLogsFromFiles(path, res);
        }
        return res;
    }

    private static void getLogsFromFiles(final String validPath, final List<String> res) {
        Path dir = Path.of(getStartDir(validPath));
        String endOfPath = validPath.substring(dir.toString().length() + 1);
        String filename = validPath.substring(validPath.lastIndexOf('/') + 1);
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir, filename)) {
            for (final Path value : directoryStream) {
                getLogsFromFile(value.toString(), res);
            }

            PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher("glob:" + endOfPath);
            Files.walkFileTree(dir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    if (pathMatcher.matches(file)) {
                        getLogsFromFile(file.toString(), res);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getLogsFromFile(final String validPath, final List<String> res) {
        try (BufferedReader reader = new BufferedReader(new FileReader(validPath))) {
            String line = reader.readLine();
            while (line != null) {
                res.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getValidPath(String path) {
        if (isUrl(path)) {
            try (HttpClient client = HttpClient.newBuilder().build()) {
                HttpRequest request = HttpRequest.newBuilder(new URI(path)).build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                Path newFile = Files.createTempFile(null, null);
                Files.writeString(newFile, response.body());
                return newFile;
            } catch (IOException | InterruptedException | URISyntaxException e) {
                return null;
            }
        }
        return Path.of(path);
    }

    private boolean isUrl(String s) {
        try {
            new URL(s);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    private static String getStartDir(String path) {
        int firstAsteriskIndex = path.indexOf("*");
        int lastSlashIndex = path.lastIndexOf("/", firstAsteriskIndex);
        return path.substring(0, lastSlashIndex);
    }
}
