package edu.project4;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageGenerator {

    private final static Logger LOGGER = LogManager.getLogger();

    private final ConfigParser configParser;

    ImageGenerator(ConfigParser configParser) {
        this.configParser = configParser;
    }

    public void createImage() {
        long start = System.currentTimeMillis();
        MultiThreadRenderer renderer = new MultiThreadRenderer(configParser.height, configParser.width);
        renderer.generateImage(configParser.iterations, configParser.threshold, configParser.transformations);
        ImageUtils imageUtils = new ImageUtils(
            configParser.pathToFile, configParser.format, renderer.image,
            configParser.window, configParser.gamma
        );
        imageUtils.saveImage();
        long end = System.currentTimeMillis();
        LOGGER.info("Время работы -- " + (end - start));
    }
}
