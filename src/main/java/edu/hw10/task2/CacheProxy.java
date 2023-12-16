package edu.hw10.task2;

import java.lang.reflect.Proxy;

public class CacheProxy {
    private CacheProxy() {
    }

    public static <T> T create(T instance, String path) {
        return (T) Proxy.newProxyInstance(
            instance.getClass().getClassLoader(),
            instance.getClass().getInterfaces(),
            new CacheInvocationHandler(instance, path)
        );
    }
}
