package edu.hw10.task1;

import edu.hw10.task1.annotations.Max;
import edu.hw10.task1.annotations.Min;
import java.lang.annotation.Annotation;
import java.util.Random;

public class RandomValueGenerator {

    private final static Random RANDOM = new Random();
    private final static int ALPHABET_LENGTH = 26;
    private final static int MAX_LENGTH_OF_RANDOM_STRING = 100;

    private RandomValueGenerator() {
    }

    static Object generateRandomValue(Class<?> type, Annotation[] annotations) {
        if (type == int.class || type == Integer.class) {
            return generateRandomInt(annotations);
        } else if (type == double.class || type == Double.class) {
            return generateRandomDouble(annotations);
        } else if (type == String.class) {
            return generateRandomString(annotations);
        }

        throw new UnsupportedOperationException("Unsupported type: " + type);
    }

    private static <T extends Annotation> T findAnnotation(Annotation[] annotations, Class<T> annotationType) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationType) {
                return (T) annotation;
            }
        }
        return null;
    }

    private static int generateRandomInt(Annotation[] annotations) {
        Min minAnnotation = findAnnotation(annotations, Min.class);
        Max maxAnnotation = findAnnotation(annotations, Max.class);

        int minValue = minAnnotation != null ? minAnnotation.value() : 1;
        int maxValue = maxAnnotation != null ? maxAnnotation.value() : Integer.MAX_VALUE;

        if (minValue > maxValue) {
            throw new IllegalArgumentException();
        }

        return RANDOM.nextInt(maxValue - minValue + 1) + minValue;
    }

    private static double generateRandomDouble(Annotation[] annotations) {
        Min minAnnotation = findAnnotation(annotations, Min.class);
        Max maxAnnotation = findAnnotation(annotations, Max.class);

        int minValue = minAnnotation != null ? minAnnotation.value() : 1;
        int maxValue = maxAnnotation != null ? maxAnnotation.value() : Integer.MAX_VALUE;

        if (minValue > maxValue) {
            throw new IllegalArgumentException();
        }

        return RANDOM.nextDouble(maxValue - minValue + 1) + minValue;
    }

    private static String generateRandomString(Annotation[] annotations) {
        Min minAnnotation = findAnnotation(annotations, Min.class);
        Max maxAnnotation = findAnnotation(annotations, Max.class);

        int minValue = minAnnotation != null ? minAnnotation.value() : 1;
        int maxValue = maxAnnotation != null ? maxAnnotation.value() : MAX_LENGTH_OF_RANDOM_STRING;

        if (minValue > maxValue) {
            throw new IllegalArgumentException();
        }

        return generateRandomString(RANDOM.nextInt(maxValue - minValue + 1) + minValue);
    }

    private static String generateRandomString(final int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char randomChar = (char) (RANDOM.nextInt(ALPHABET_LENGTH) + 'a');
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
