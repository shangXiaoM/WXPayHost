package com.rencare.pay.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BeanTools {

    public static Class<?> getGenericClass(Class<?> clazz) {
        return getGenericClass(clazz, 0);
    }

    public static Class<?> getGenericClass(Class<?> clazz, int index) throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size of Parameterized Type: " + params.length);
        }
        return (Class<?>) params[index];
    }

}
