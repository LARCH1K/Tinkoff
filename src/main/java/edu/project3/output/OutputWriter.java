package edu.project3.output;

import edu.project3.LogStatistics;

public interface OutputWriter {
    StringBuilder writeResult(LogStatistics logStatistics);
}
