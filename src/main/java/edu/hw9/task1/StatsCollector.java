package edu.hw9.task1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StatsCollector {
    private final ExecutorService executor;
    private final List<Future<Void>> futures;
    private final List<Statistic> statistics;

    public StatsCollector(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.futures = new ArrayList<>();
        this.statistics = new ArrayList<>();
    }

    public void push(String metricName, double[] data) {
        Future<Void> future = executor.submit(() -> {
            double sum = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;

            for (double value : data) {
                sum += value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            if (data.length == 0) {
                min = 0;
                max = 0;
            }
            synchronized (statistics) {
                statistics.add(new Statistic(metricName, sum, sum / data.length, min, max));
            }

            return null;
        });

        futures.add(future);
    }

    public List<Statistic> stats() {
        try {
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdown();
        }

        statistics.sort(Comparator.comparing(Statistic::metricName));

        return statistics;
    }
}
