package edu.project4;

import edu.project4.models.Pixel;
import edu.project4.models.Point;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import static java.lang.Math.abs;

public class MultiThreadRenderer {
    final int height;
    final int width;
    int threadCount = Runtime.getRuntime().availableProcessors();
    final List<Thread> threads = new ArrayList<>();
    final Pixel[][] image;

    MultiThreadRenderer(int height, int width) {
        this.height = height;
        this.width = width;
        this.image =
            Stream.generate(() -> Stream.generate(() -> new Pixel(Color.BLACK)).limit(width).toArray(Pixel[]::new))
                .limit(height)
                .toArray(Pixel[][]::new);
    }

    void generateImage(int iterations, double threshold, Transformations transformations) {
        int iterationsPerThread = iterations / threadCount;
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(() -> doIterations(threshold, iterationsPerThread, transformations));
            thread.start();
            threads.add(thread);
        }
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Не удалось закончить выполнение потока!", e);
            }
        });
    }

    private void doIterations(double threshold, int iterationsPerThread, Transformations transformations) {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        Point randomPoint = getRandomPoint(random);
        for (int i = 0; i < iterationsPerThread; i++) {
            randomPoint = transformations.functionList
                .get(getRandomFlameFunction(random, transformations)).apply(randomPoint);
            while (abs(randomPoint.point.getX()) >= threshold || abs(randomPoint.point.getY()) >= threshold) {
                randomPoint = getRandomPoint(random);
                randomPoint = transformations.functionList
                    .get(getRandomFlameFunction(random, transformations)).apply(randomPoint);
            }
            int x = (int) (Math.floor((randomPoint.point.getX() / threshold) * height / 2.0) + height / 2.0);
            int y = (int) (Math.floor((randomPoint.point.getY() / threshold) * width / 2.0) + width / 2.0);
            synchronized (image[x][y]) {
                image[x][y].hitCount++;
                image[x][y].color = randomPoint.color;
            }
        }
    }

    private Point getRandomPoint(ThreadLocalRandom random) {
        final double h = random.nextDouble(-1.0, 1.0);
        final double w = random.nextDouble(-1.0, 1.0);
        final int r = random.nextInt(256);
        final int g = random.nextInt(256);
        final int b = random.nextInt(256);
        return new Point(new Point2D.Double(h, w), new Color(r, g, b));
    }

    private int getRandomFlameFunction(ThreadLocalRandom random, Transformations transformations) {
        final int size = transformations.functionList.size();
        double randomValue = random.nextDouble();
        for (int i = 0; i < size - 1; i++) {
            randomValue -= transformations.probabilities.get(i);
            if (randomValue < 0) {
                return i;
            }
        }
        return size - 1;
    }
}
