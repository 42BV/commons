/*
 * (C) 2013 42 bv (www.42.nl). All rights reserved.
 */
package nl.mad.commons;

import static nl.mad.commons.ConstructorFinder.findMostSupportedConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class for handling objects.
 *
 * @author Jeroen van Schagen
 * @since Feb 7, 2014
 */
public final class Objects {
    
    private Objects() {
    }
    
    public static <T> T instantiate(Class<T> beanClass, Object... arguments) {
        Class<?>[] argumentTypes = getArgumentTypes(arguments);
        Constructor<T> constructor = findMostSupportedConstructor(beanClass, argumentTypes);
        return instantiate(constructor, arguments);
    }

    public static Class<?>[] getArgumentTypes(Object... arguments) {
        Class<?>[] types = new Class<?>[arguments.length];
        for (int index = 0; index < types.length; index++) {
            types[index] = arguments[index].getClass();
        }
        return types;
    }

    public static <T> T instantiate(Constructor<T> constructor, Object... arguments) {
        Object[] invocationArguments = putInCorrectOrder(constructor.getParameterTypes(), arguments);
        try {
            return constructor.newInstance(invocationArguments);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Could not instantiate constructor.", e);
        }
    }
    
    private static Object[] putInCorrectOrder(Class<?>[] types, Object[] arguments) {
        Object[] result = new Object[types.length];
        for (int index = 0; index < types.length; index++) {
            result[index] = findFirstOfType(types[index], arguments);
        }
        return result;
    }
    
    private static Object findFirstOfType(Class<?> requiredType, Object[] values) {
        for (Object value : values) {
            if (isOfType(value, requiredType)) {
                return value;
            }
        }
        return null;
    }
    
    private static boolean isOfType(Object value, Class<?> requiredType) {
        return value != null && requiredType.isAssignableFrom(value.getClass());
    }

}
