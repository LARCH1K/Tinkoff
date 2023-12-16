package edu.hw10.task1.classes;

import edu.hw10.task1.annotations.Max;
import edu.hw10.task1.annotations.Min;

public class MyClass {
    private final int intValue;

    public MyClass(@Min(1) @Max(100) int intValue) {
        this.intValue = intValue;
    }

    public static MyClass create(@Min(100) @Max(200) int intValue) {
        return new MyClass(intValue);
    }

    public int getIntValue() {
        return intValue;
    }
}
