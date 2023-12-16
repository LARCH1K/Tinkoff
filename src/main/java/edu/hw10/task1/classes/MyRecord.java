package edu.hw10.task1.classes;

import edu.hw10.task1.annotations.Max;
import edu.hw10.task1.annotations.Min;
import edu.hw10.task1.annotations.NotNull;

public record MyRecord(
    @NotNull String stringValue, @Min(1) @Max(100) int intValue, @Min(100) @Max(200) int doubleValue
) {
}
