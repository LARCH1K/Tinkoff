package edu.hw11;

import edu.hw11.task3.ByteClassLoader;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static edu.hw11.task3.GeneratorFibonacciClass.generateFibonacciClass;

public class Task3Test {

    @Test
    void task3Test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] classBytes = generateFibonacciClass();
        Class<?> fibonacciClass = new ByteClassLoader().defineClass("Fibonacci", classBytes);

        long result = (long) fibonacciClass.getMethod("fib", int.class).invoke(null, 5);
        Assertions.assertEquals(5, result);

        result = (long) fibonacciClass.getMethod("fib", int.class).invoke(null, 10);
        Assertions.assertEquals(55, result);

        result = (long) fibonacciClass.getMethod("fib", int.class).invoke(null, 47);
        Assertions.assertEquals(2971215073L, result);
    }
}

