package edu.project3;

import edu.project3.output.OutputFormatter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("ModifiedControlVariable")
public class NginxLogAnalyzer {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Logger LOGGER = LogManager.getLogger();

    public void analyzeLogs(String[] args) {
        String path = "";
        String fromDateStr = null;
        String toDateStr = null;
        String outPath = "src/main/resources/project3";
        String format = "markdown";

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--path":
                    path = args[++i];
                    break;
                case "--from":
                    fromDateStr = args[++i];
                    break;
                case "--to":
                    toDateStr = args[++i];
                    break;
                case "--format":
                    format = args[++i];
                    break;
                case "--out":
                    outPath = args[++i];
                    break;
                default:
                    LOGGER.info("Unknown option: " + args[i]);
                    System.exit(1);
            }
        }

        LocalDate fromDate =
            fromDateStr != null ? LocalDate.parse(fromDateStr, FORMATTER) : null;
        LocalDate toDate =
            toDateStr != null ? LocalDate.parse(toDateStr, FORMATTER) : null;

        LogFileReader logFileReader = new LogFileReader();
        LogParser logParser = new LogParser();
        List<String> logLines = logFileReader.readLogs(path);

        List<LogEntry> logEntries = logLines.stream()
            .map(logParser::parseLogEntry)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        LogStatistics logStatistics = new LogStatistics(logEntries, fromDate, toDate);

        OutputFormatter outputFormatter = new OutputFormatter();
        String formattedResult = outputFormatter.formatResults(logStatistics, format);

        try {
            if (format.equalsIgnoreCase("adoc")) {
                Files.write(Path.of(outPath + "/result.adoc"), formattedResult.getBytes());
            } else {
                Files.write(Path.of(outPath + "/result.md"), formattedResult.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
