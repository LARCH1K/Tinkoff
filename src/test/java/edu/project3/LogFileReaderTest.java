package edu.project3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogFileReaderTest {

    @Test
    void logFileReaderTest(){
        LogFileReader fileReader = new LogFileReader();
        Assertions.assertEquals(6, fileReader.readLogs("src/main/resources/project3/testPr3/dirPr3/2045.txt").size());
        Assertions.assertEquals(6, fileReader.readLogs("src/main/resources/project3/**/2045.txt").size());
        Assertions.assertEquals(4, fileReader.readLogs("src/main/resources/project3/testPr3/20*").size());
        Assertions.assertEquals(13, fileReader.readLogs("src/main/resources/project3/testPr3/dirPr3/20*").size());
        Assertions.assertEquals(6, fileReader.readLogs("src/main/resources/project3/testPr3/dirPr3/*.txt").size());
        Assertions.assertFalse(fileReader.readLogs("https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs").isEmpty());
    }
}
