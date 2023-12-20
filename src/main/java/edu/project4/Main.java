package edu.project4;

import java.nio.file.Path;

public class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ConfigParser configParser = new ConfigParser(Path.of("src/main/java/edu/project4/config.txt"));
        ImageGenerator generator = new ImageGenerator(configParser);
        generator.createImage();
    }
}
