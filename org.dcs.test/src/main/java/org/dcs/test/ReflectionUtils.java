package org.dcs.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by cmathew on 27/01/16.
 */
public class ReflectionUtils {

  public static Object invokeMethod(Object object, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = object.getClass().getDeclaredMethod(methodName);
    method.setAccessible(true);
    return method.invoke(object);
  }
}
