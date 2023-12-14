package edu.hw9;

import edu.hw9.task1.Statistic;
import edu.hw9.task1.StatsCollector;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task1Test {

    @Test
    void statsCollectorTest() {
        StatsCollector statsCollector = new StatsCollector(5);
        statsCollector.push("metric1", new double[] {1.0, 2.0, 3.0});
        statsCollector.push("metric2", new double[] {4.0, 5.0, 6.0});
        statsCollector.push("metric3", new double[] {0.1, 0.1, 1.4, 0.3});
        statsCollector.push("metric4", new double[] {2.0, 1.5, 0.8, 4.2, 3.7});
        statsCollector.push("metric5", new double[] {2.5, 1.2, 0.3, 4.4, 8.7});

        List<Statistic> statistics = statsCollector.stats();
        assertEquals(5, statistics.size());

        Statistic stat1 = statistics.get(0);
        assertEquals("metric1", stat1.metricName());
        assertEquals(6.0, stat1.sum());
        assertEquals(2.0, stat1.average());
        assertEquals(1.0, stat1.min());
        assertEquals(3.0, stat1.max());

        Statistic stat2 = statistics.get(1);
        assertEquals("metric2", stat2.metricName());
        assertEquals(15.0, stat2.sum());
        assertEquals(5.0, stat2.average());
        assertEquals(4.0, stat2.min());
        assertEquals(6.0, stat2.max());

        Statistic stat3 = statistics.get(2);
        assertEquals("metric3", stat3.metricName());
        assertEquals(1.9, stat3.sum());
        assertEquals(0.475, stat3.average());
        assertEquals(0.1, stat3.min());
        assertEquals(1.4, stat3.max());
    }

    @Test
    void testStatsCollectorWithEmptyData() {
        StatsCollector statsCollector = new StatsCollector(2);
        statsCollector.push("emptyMetric", new double[] {});

        List<Statistic> statistics = statsCollector.stats();
        assertEquals(1, statistics.size());

        Statistic stat = statistics.get(0);
        assertEquals("emptyMetric", stat.metricName());
        assertEquals(0.0, stat.sum());
        assertEquals(Double.NaN, stat.average());
        assertEquals(0.0, stat.min());
        assertEquals(0.0, stat.max());
    }
}
