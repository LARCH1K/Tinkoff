package edu.project3;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;

public class LogStatisticsTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern(
        "dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    @Test
    void logStatisticsTest(){
        LocalDate fromDate = LocalDate.parse("2015-05-18", FORMATTER);
        LocalDate toDate = LocalDate.parse("2023-11-12", FORMATTER);

        LogParser logParser = new LogParser();
        List<String> logLines = List.of(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\"" +
                " 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"",
            "217.168.17.5 - - [17/May/2015:08:05:09 +0000] \"GET /downloads/product_2 HTTP/1.1\"" +
                " 200 490 \"-\" \"Debian APT-HTTP/1.3 (0.8.10.3)\"",
            "37.187.252.103 - - [21/May/2015:05:05:57 +0000] \"GET /downloads/product_2 HTTP/1.1\"" +
                " 404 318 \"-\" \"Debian APT-HTTP/1.3 (1.0.1ubuntu2)\"",
            "80.94.65.132 - - [21/May/2015:15:05:30 +0000] \"GET /downloads/product_2 HTTP/1.1\"" +
                " 304 0 \"-\" \"Debian APT-HTTP/1.3 (1.0.1ubuntu2)\"",
            "37.187.136.53 - - [28/May/2015:12:05:34 +0000] \"GET /downloads/product_2 HTTP/1.1\"" +
                " 304 0 \"-\" \"Debian APT-HTTP/1.3 (0.9.7.9)\"",
            "17.72.215.118 - - [13/Nov/2023:11:32:20 +0000] \"GET" +
                " /Function-based_core-Open-architected-workforce%20Right-sized.svg HTTP/1.1\" " +
                "200 2337 \"-\" \"Opera/8.39 (Macintosh; Intel Mac OS X 10_9_9; en-US) Presto/2.12.292 Version/10.00\""
        );

        List<LogEntry> logEntries = logLines.stream()
            .map(logParser::parseLogEntry)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        LogStatistics logStatistics = new LogStatistics(logEntries, fromDate, toDate);

        assertThat(logStatistics.getResourceRequests()).containsEntry("/downloads/product_2", 3);
        assertThat(logStatistics.getResourceRequests().size()).isEqualTo(1);

        assertThat(logStatistics.getResponseCodes()).containsEntry(404, 1);
        assertThat(logStatistics.getResponseCodes()).containsEntry(304, 2);
        assertThat(logStatistics.getResponseCodes().size()).isEqualTo(2);

        assertThat(logStatistics.getPopularHttpMethods()).isEqualTo(new LogStatistics.CountHttpMethods[]
            {new LogStatistics.CountHttpMethods("GET", 3)});

        assertThat(logStatistics.getPopularDates()).isEqualTo(new LogStatistics.CountDates[]
            {
                new LogStatistics.CountDates(LocalDate.parse("21/May/2015:05:05:57 +0000", FORMATTER2), 2),
                new LogStatistics.CountDates(LocalDate.parse("28/May/2015:12:05:34 +0000", FORMATTER2), 1)
            });

        assertThat(logStatistics.getPopularUserAgent()).isEqualTo(new LogStatistics.CountUserAgent[]
            {
                new LogStatistics.CountUserAgent("Debian APT-HTTP/1.3 (1.0.1ubuntu2)", 2),
                new LogStatistics.CountUserAgent("Debian APT-HTTP/1.3 (0.9.7.9)", 1)
            });

        assertThat(logStatistics.getAverageResponseSize()).isEqualTo(106.0);
        assertThat(logStatistics.getTotalRequests()).isEqualTo(3);
    }
}
