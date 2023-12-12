package edu.project3;

import java.time.LocalDate;

public record LogEntry(String remoteAddr,
                       LocalDate timestamp,
                       String httpMethod,
                       String uri,
                       String version,
                       int status,
                       long bodyBytesSent,
                       String userAgent) {

}
