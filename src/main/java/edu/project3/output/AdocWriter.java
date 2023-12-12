package edu.project3.output;

import edu.project3.LogStatistics;

@SuppressWarnings("MultipleStringLiterals")
public class AdocWriter implements OutputWriter {
    @Override
    public StringBuilder writeResult(final LogStatistics logStatistics) {
        StringBuilder result = new StringBuilder();

        appendGeneralInfo(logStatistics, result);
        appendResoursec(logStatistics, result);
        appendCodes(logStatistics, result);
        appendMethods(logStatistics, result);
        appendDates(logStatistics, result);
        appendUserAgents(logStatistics, result);

        return result;
    }

    private void appendUserAgents(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("== UserAgents\n\n");
        result.append("|===\n");
        result.append("| UserAgent | Количество\n");
        for (LogStatistics.CountUserAgent entry : logStatistics.getPopularUserAgent()) {
            result.append("| ").append(entry.userAgent()).append("  | ").append(entry.count()).append("\n");
        }
        result.append("|===\n\n");
    }

    private void appendDates(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("== Даты запросов\n\n");
        result.append("|===\n");
        result.append("| Дата | Количество\n");
        for (LogStatistics.CountDates entry : logStatistics.getPopularDates()) {
            result.append("| ").append(entry.date()).append("  | ").append(entry.count()).append("\n");
        }
        result.append("|===\n\n");
    }

    private void appendMethods(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("== Методы запросов\n\n");
        result.append("|===\n");
        result.append("| Метод запроса | Количество\n");
        for (LogStatistics.CountHttpMethods entry : logStatistics.getPopularHttpMethods()) {
            result.append("| ").append(entry.method()).append("  | ").append(entry.count()).append("\n");
        }
        result.append("|===\n\n");
    }

    private void appendCodes(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("== Коды ответа\n\n");
        result.append("|===\n");
        result.append("| Код | Количество\n");
        for (LogStatistics.CountResponseCodes entry : logStatistics.getPopularResponseCodes()) {
            result.append("| ").append(entry.resource()).append(" | ").append(entry.count()).append("\n");
        }
        result.append("|===\n\n");
    }

    private void appendResoursec(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("== Запрашиваемые ресурсы\n\n");
        result.append("|===\n");
        result.append("| Ресурс | Количество\n");
        for (LogStatistics.CountResourceRequests entry : logStatistics.getPopularResourceRequests()) {
            result.append("| ").append(entry.resource()).append("  | ").append(entry.count()).append("\n");
        }
        result.append("|===\n\n");
    }

    private void appendGeneralInfo(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("= Log Analysis Report\n\n");
        result.append("== Общая информация\n\n");
        result.append("|===\n");
        result.append("| Метрика | Значение\n");
        result.append("| Начальная дата | ")
            .append(logStatistics.getFromDate() == null ? "-" : logStatistics.getFromDate()).append("\n");
        result.append("| Конечная дата | ").append(logStatistics.getToDate() == null ? "-" : logStatistics.getToDate())
            .append("\n");
        result.append("| Количество запросов | ").append(logStatistics.getTotalRequests()).append("\n");
        result.append("| Средний размер ответа | ")
            .append(String.format("%.2f", logStatistics.getAverageResponseSize()).replace(',', '.')).append("b\n");
        result.append("|===\n\n");
    }
}
