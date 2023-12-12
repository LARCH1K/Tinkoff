package edu.project3.output;

import edu.project3.LogStatistics;

public class OutputFormatter {
    public String formatResults(LogStatistics logStatistics, String format) {
        OutputWriter outputWriter;
        return switch (format.toLowerCase()) {
            case "markdown" -> {
                outputWriter = new MdWriter();
                yield outputWriter.writeResult(logStatistics).toString();
            }
            case "adoc" -> {
                outputWriter = new AdocWriter();
                yield outputWriter.writeResult(logStatistics).toString();
            }
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }
}
