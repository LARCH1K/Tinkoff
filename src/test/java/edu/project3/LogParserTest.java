package edu.project3;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogParserTest {

    @Test
    void logParserTest(){
        LogParser logParser = new LogParser();

        String log = "133.123.215.3 - - [13/Nov/2023:11:32:20 +0000] \"GET /Team-oriented_heuristic-Phased.js" +
            " HTTP/1.1\" 200 2781 \"-\" \"Mozilla/5.0 (Windows NT 6.2) AppleWebKit/5351 (KHTML, like Gecko)" +
            " Chrome/40.0.883.0 Mobile Safari/5351\"";
        LogEntry expected = new LogEntry("133.123.215.3"
            , LocalDate.parse("13/Nov/2023:11:32:20 +0000", DateTimeFormatter.ofPattern(
                "dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH))
            , "GET", "/Team-oriented_heuristic-Phased.js", "HTTP/1.1", 200
            , 2781
            , "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/5351" +
            " (KHTML, like Gecko) Chrome/40.0.883.0 Mobile Safari/5351");

        assertEquals(expected, logParser.parseLogEntry(log));

        log = "17.72.215.118 - - [13/Nov/2023:11:32:20 +0000] \"GET " +
            "/Function-based_core-Open-architected-workforce%20Right-sized.svg HTTP/1.1\" 200 2337 \"-\" " +
            "\"Opera/8.39 (Macintosh; Intel Mac OS X 10_9_9; en-US) Presto/2.12.292 Version/10.00\"";
        expected = new LogEntry("17.72.215.118"
            , LocalDate.parse("13/Nov/2023:11:32:20 +0000", DateTimeFormatter.ofPattern(
            "dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH))
            , "GET", "/Function-based_core-Open-architected-workforce%20Right-sized.svg"
            , "HTTP/1.1", 200, 2337
            , "Opera/8.39 (Macintosh; Intel Mac OS X 10_9_9; en-US) Presto/2.12.292 Version/10.00");

        assertEquals(expected, logParser.parseLogEntry(log));
    }
}
