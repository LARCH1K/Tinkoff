package edu.project3.output;

import edu.project3.LogStatistics;

@SuppressWarnings("MultipleStringLiterals")
public class MdWriter implements OutputWriter {
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

    private static void appendUserAgents(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("\n#### UserAgents\n\n");
        result.append("| UserAgent             | Количество |\n");
        result.append("|:---------------------:|-----------:|\n");
        for (LogStatistics.CountUserAgent entry : logStatistics.getPopularUserAgent()) {
            result.append("| ").append(entry.userAgent()).append("  | ").append(entry.count()).append(" |\n");
        }
    }

    private static void appendDates(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("\n#### Даты запросов\n\n");
        result.append("| Дата                  | Количество |\n");
        result.append("|:---------------------:|-----------:|\n");
        for (LogStatistics.CountDates entry : logStatistics.getPopularDates()) {
            result.append("| ").append(entry.date()).append("  | ").append(entry.count()).append(" |\n");
        }
    }

    private static void appendMethods(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("\n#### Методы запросов\n\n");
        result.append("| Метод запроса         | Количество |\n");
        result.append("|:---------------------:|-----------:|\n");
        for (LogStatistics.CountHttpMethods entry : logStatistics.getPopularHttpMethods()) {
            result.append("| ").append(entry.method()).append("  | ").append(entry.count()).append(" |\n");
        }
    }

    private static void appendCodes(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("\n#### Коды ответа\n\n");
        result.append("| Код | Количество |\n");
        result.append("|:---:|-----------:|\n");
        for (LogStatistics.CountResponseCodes entry : logStatistics.getPopularResponseCodes()) {
            result.append("| ").append(entry.resource()).append(" | ").append(entry.count()).append(" |\n");
        }
    }

    private static void appendResoursec(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("\n#### Запрашиваемые ресурсы\n\n");
        result.append("| Ресурс                | Количество |\n");
        result.append("|:---------------------:|-----------:|\n");
        for (LogStatistics.CountResourceRequests entry : logStatistics.getPopularResourceRequests()) {
            result.append("| ").append(entry.resource()).append("  | ").append(entry.count()).append(" |\n");
        }
    }

    private static void appendGeneralInfo(final LogStatistics logStatistics, final StringBuilder result) {
        result.append("#### Общая информация\n\n");
        result.append("| Метрика              | Значение   |\n");
        result.append("|:--------------------:|-----------:|\n");
        result.append("| Начальная дата       | ")
            .append(logStatistics.getFromDate() == null ? "-" : logStatistics.getFromDate()).append(" |\n");
        result.append("| Конечная дата        | ")
            .append(logStatistics.getToDate() == null ? "-" : logStatistics.getToDate()).append(" |\n");
        result.append("| Количество запросов  | ").append(logStatistics.getTotalRequests()).append(" |\n");
        result.append("| Средний размер ответа| ")
            .append(String.format("%.2f", logStatistics.getAverageResponseSize()).replace(',', '.'))
            .append("b |\n");
    }
}
