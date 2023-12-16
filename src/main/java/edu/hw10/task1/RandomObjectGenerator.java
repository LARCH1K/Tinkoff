package edu.hw10.task1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import static edu.hw10.task1.RandomValueGenerator.generateRandomValue;

public class RandomObjectGenerator {

    public <T> T nextObject(Class<T> clazz, String factoryMethod)
        throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        if (factoryMethod != null) {
            Method method = clazz.getDeclaredMethod(factoryMethod, getParameterTypes(clazz, factoryMethod));
            method.setAccessible(true);
            return (T) method.invoke(
                null,
                generateRandomValues(method.getParameterTypes(), method.getParameterAnnotations())
            );
        } else {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            return (T) constructor.newInstance(generateRandomValues(
                constructor.getParameterTypes(),
                constructor.getParameterAnnotations()
            ));
        }
    }

    public <T> T nextObject(Class<T> clazz)
        throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        return (T) constructor.newInstance(generateRandomValues(
            constructor.getParameterTypes(),
            constructor.getParameterAnnotations()
        ));
    }

    private Object[] generateRandomValues(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        Object[] values = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            values[i] = generateRandomValue(parameterTypes[i], parameterAnnotations[i]);
        }
        return values;
    }

    private Class<?>[] getParameterTypes(Class<?> clazz, String methodName) throws NoSuchMethodException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method.getParameterTypes();
            }
        }
        throw new NoSuchMethodException("Method not found: " + methodName);
    }
}
