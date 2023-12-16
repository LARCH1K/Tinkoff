package edu.hw11;

import edu.hw11.task2.ArithmeticUtils;
import edu.hw11.task2.ArithmeticUtilsInterceptor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Task2Test {

    @Test
    void task2Test() {
        TypeDescription typeDescription = TypePool.Default.ofSystemLoader()
            .describe("edu.hw11.task2.ArithmeticUtils")
            .resolve();
        new ByteBuddy()
            .redefine(typeDescription, ClassFileLocator.ForClassLoader.ofSystemLoader())
            .method(ElementMatchers.named("sum"))
            .intercept(MethodDelegation.to(new ArithmeticUtilsInterceptor()))
            .make()
            .load(ClassLoader.getSystemClassLoader(), ClassLoadingStrategy.Default.INJECTION);

        ArithmeticUtils arithmeticUtils = new ArithmeticUtils();
        Assertions.assertEquals(12, arithmeticUtils.sum(3, 4));
        Assertions.assertEquals(24, arithmeticUtils.sum(3, 8));
        Assertions.assertEquals(36, arithmeticUtils.sum(9, 4));
    }
}
