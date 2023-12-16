package edu.hw10.task2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class CacheInvocationHandler implements InvocationHandler {

    private final Object target;
    private final Map<String, Object> cache;
    private final String path;
    private final ObjectMapper mapper;

    CacheInvocationHandler(Object target, final String path) {
        this.target = target;
        this.path = path;
        this.mapper = new ObjectMapper();
        Map<String, Object> map;
        try {
            map = mapper.readValue(
                new File(path),
                new TypeReference<ConcurrentHashMap<String, Object>>() {
                }
            );
        } catch (IOException e) {
            map = new ConcurrentHashMap<>();
        }
        this.cache = map;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(Cache.class)) {
            Cache cacheAnnotation = method.getAnnotation(Cache.class);
            String key = generateCacheKey(method, args);

            if (cache.containsKey(key)) {
                Object res = cache.get(key);
                if (res instanceof Number) {
                    return castToNeedNumberClass(res, method.getReturnType());
                }
                return res;
            } else {
                Object result = method.invoke(target, args);

                cache.put(key, result);
                if (cacheAnnotation.persist()) {
                    persistToDisk();
                }

                return result;
            }
        } else {
            return method.invoke(target, args);
        }
    }

    private Object castToNeedNumberClass(final Object object, final Class<?> returnType) {
        Object result = object;

        if (returnType.equals(Long.class) || returnType.equals(long.class)) {
            result = ((Number) object).longValue();
        }
        if (returnType.equals(Short.class) || returnType.equals(short.class)) {
            result = ((Number) object).shortValue();
        }
        if (returnType.equals(Integer.class) || returnType.equals(int.class)) {
            result = ((Number) object).intValue();
        }
        if (returnType.equals(Float.class) || returnType.equals(float.class)) {
            result = ((Number) object).floatValue();
        }
        if (returnType.equals(Double.class) || returnType.equals(double.class)) {
            result = ((Number) object).doubleValue();
        }
        if (returnType.equals(Byte.class) || returnType.equals(byte.class)) {
            result = ((Number) object).byteValue();
        }

        return result;
    }

    private String generateCacheKey(Method method, Object[] args) {
        StringBuilder key = new StringBuilder(method.getName());
        for (Object arg : args) {
            key.append("_").append(arg.toString());
        }
        return key.toString();
    }

    private void persistToDisk() {
        try {
            mapper.writeValue(new File(path), cache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
