package edu.project4;

import edu.project4.models.Pixel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public class ImageUtils {
    final Path path;
    final String format;
    Pixel[][] image;
    final int window;
    final double gamma;
    int maxCount = 0;

    ImageUtils(Path path, String format, Pixel[][] image, int window, double gamma) {
        this.path = path;
        this.format = format;
        this.image = image;
        this.window = window;
        this.gamma = gamma;
    }

    private Pixel[][] averagePool() {
        int height = image.length;
        int width = image[0].length;
        var averagedMap = new Pixel[height / window][width / window];
        int half = window / 2;
        int total = window * window;
        for (int i = half; i < height - half; i += window) {
            for (int j = half; j < width - half; j += window) {
                calcAvg(averagedMap, half, total, i, j);
            }
        }
        return averagedMap;
    }

    void saveImage() {
        image = averagePool();
        gammaCorrection();

        BufferedImage bufferedImage = new BufferedImage(
            image.length, image[0].length,
            BufferedImage.TYPE_INT_RGB
        );
        for (int x = 0; x < image.length; x++) {
            for (int y = 0; y < image[0].length; y++) {
                bufferedImage.setRGB(x, y, image[x][y].color.getRGB());
            }
        }
        try {
            File outputfile = new File(path.toUri());
            ImageIO.write(bufferedImage, format, outputfile);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить изображение!", e);
        }
    }

    private void gammaCorrection() {
        for (final Pixel[] pixels : image) {
            for (int j = 0; j < image[0].length; j++) {
                int r = pixels[j].color.getRed();
                int g = pixels[j].color.getGreen();
                int b = pixels[j].color.getBlue();
                double coefficient = Math.pow(Math.log(pixels[j].hitCount) / Math.log(maxCount), 1.0 / gamma);
                r *= coefficient;
                g *= coefficient;
                b *= coefficient;
                pixels[j].color = new Color(r, g, b);
            }
        }
    }

    private void calcAvg(Pixel[][] averagedMap, int half, int total, int i, int j) {
        int countHits = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i1 = i - half; i1 <= i + half; i1++) {
            for (int j1 = j - half; j1 <= j + half; j1++) {
                if (image[i1][j1] == null) {
                    image[i1][j1] = new Pixel(Color.BLACK);
                }
                countHits += image[i1][j1].hitCount;
                r += image[i1][j1].color.getRed();
                g += image[i1][j1].color.getGreen();
                b += image[i1][j1].color.getBlue();
            }
        }
        averagedMap[(i - half) / window][(j - half) / window] =
            new Pixel(new Color(r / total, g / total, b / total));
        averagedMap[(i - half) / window][(j - half) / window].hitCount = Math.ceilDiv(countHits, total);
        maxCount = Math.max(maxCount, Math.ceilDiv(countHits, total));
    }
}
