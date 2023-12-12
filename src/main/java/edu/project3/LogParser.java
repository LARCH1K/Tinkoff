package edu.project3;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("MagicNumber")
public class LogParser {
    private static final String LOG_PATTERN = "^([^ ]*) - - \\[(.*)] \"([^ ]*) ([^ ]*) ([^ ]*)\" "
        + "(-|[0-9]*) (-|[0-9]*) \"(.+?|-)\" \"(.+?|-)\"$";

    public LogEntry parseLogEntry(String logLine) {
        Pattern pattern = Pattern.compile(LOG_PATTERN);
        Matcher matcher = pattern.matcher(logLine);

        if (matcher.matches()) {
            String remoteAddr = matcher.group(1);

            LocalDate timestamp = LocalDate.parse(matcher.group(2), DateTimeFormatter.ofPattern(
                    "dd/MMM/yyyy:HH:mm:ss Z",
                    Locale.ENGLISH
                )
            );
            String httpMethod = matcher.group(3);
            String uri = matcher.group(4);
            String version = matcher.group(5);
            int status = Integer.parseInt(matcher.group(6));
            long bodyBytesSent = Long.parseLong(matcher.group(7));
            String userAgent = matcher.group(9);

            return new LogEntry(remoteAddr, timestamp, httpMethod, uri, version, status, bodyBytesSent, userAgent);
        } else {
            return null;
        }
    }
}
