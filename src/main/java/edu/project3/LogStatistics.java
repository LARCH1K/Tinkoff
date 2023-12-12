package edu.project3;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogStatistics {

    private final static int COUNT_POPULAR_ELEMENTS = 5;

    private final List<LogEntry> filteredLogs;
    private final Map<String, Integer> resourceRequests;
    private final Map<Integer, Integer> responseCodes;
    private final Map<String, Integer> httpMethods;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public LogStatistics(
        List<LogEntry> logEntries, LocalDate fromDate, LocalDate toDate
    ) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.filteredLogs = logEntries.stream()
            .filter(entry -> (fromDate == null || entry.timestamp().isAfter(fromDate))
                && (toDate == null || entry.timestamp().isBefore(toDate)))
            .collect(Collectors.toList());

        resourceRequests = filteredLogs.stream()
            .collect(Collectors.groupingBy(LogEntry::uri, Collectors.summingInt(e -> 1)));

        httpMethods = filteredLogs.stream()
            .collect(Collectors.groupingBy(LogEntry::httpMethod, Collectors.summingInt(e -> 1)));

        responseCodes = filteredLogs.stream()
            .collect(Collectors.groupingBy(LogEntry::status, Collectors.summingInt(e -> 1)));

    }

    public int getTotalRequests() {
        return filteredLogs.size();
    }

    public Map<String, Integer> getResourceRequests() {
        return resourceRequests;
    }

    public Map<Integer, Integer> getResponseCodes() {
        return responseCodes;
    }

    public double getAverageResponseSize() {
        if (!filteredLogs.isEmpty()) {
            return filteredLogs.stream()
                .mapToLong(LogEntry::bodyBytesSent)
                .average()
                .orElse(0);
        } else {
            return 0;
        }
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public CountResourceRequests[] getPopularResourceRequests() {
        return resourceRequests.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(COUNT_POPULAR_ELEMENTS)
            .map(x -> new CountResourceRequests(x.getKey(), x.getValue()))
            .toArray(CountResourceRequests[]::new);
    }

    public CountHttpMethods[] getPopularHttpMethods() {
        return httpMethods.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(COUNT_POPULAR_ELEMENTS)
            .map(x -> new CountHttpMethods(x.getKey(), x.getValue()))
            .toArray(CountHttpMethods[]::new);
    }

    public CountResponseCodes[] getPopularResponseCodes() {
        return responseCodes.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(COUNT_POPULAR_ELEMENTS)
            .map(x -> new CountResponseCodes(x.getKey(), x.getValue()))
            .toArray(CountResponseCodes[]::new);
    }

    public CountDates[] getPopularDates() {
        return filteredLogs.stream()
            .collect(Collectors.groupingBy(LogEntry::timestamp, Collectors.summingInt(e -> 1)))
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(COUNT_POPULAR_ELEMENTS)
            .map(x -> new CountDates(x.getKey(), x.getValue()))
            .toArray(CountDates[]::new);
    }

    public CountUserAgent[] getPopularUserAgent() {
        return filteredLogs.stream()
            .collect(Collectors.groupingBy(LogEntry::userAgent, Collectors.summingInt(e -> 1)))
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(COUNT_POPULAR_ELEMENTS)
            .map(x -> new CountUserAgent(x.getKey(), x.getValue()))
            .toArray(CountUserAgent[]::new);
    }

    public record CountUserAgent(String userAgent, long count) {
    }

    public record CountDates(LocalDate date, long count) {
    }

    public record CountHttpMethods(String method, long count) {
    }

    public record CountResourceRequests(String resource, long count) {
    }

    public record CountResponseCodes(Integer resource, long count) {
    }

}
