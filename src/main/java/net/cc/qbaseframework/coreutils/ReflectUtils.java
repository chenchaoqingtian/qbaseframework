package net.cc.qbaseframework.coreutils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <p/>Description：反射调用工具类
 * <p/>author： chen chao
 */
public class ReflectUtils {

    /**
     * 实例化对象
     * @param className 类路径
     * @return
     */
    public static Object newInstance(ClassLoader classLoader, String className) {

        Object instance = null;

        try {
            Class<?> clazz =  classLoader.loadClass(className);
            instance = clazz.newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 实例化对象
     * @param className 类路径
     * @param paramTypes 参数类型
     * @param params 参数
     * @return
     */
    public static Object newInstance(ClassLoader classLoader,
                                     String className,
                                     Class<?>[] paramTypes,
                                     Object[] params) {
        Object instance = null;
        try {
            Class<?> clazz =  classLoader.loadClass(className);
            Constructor con = clazz.getConstructor(paramTypes);
            instance = con.newInstance(params);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 调用公共静态方法
     * @param className 类路径
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static Object invokePublicStaticMethod(ClassLoader classLoader,
                                                  String className,
                                                  String methodName,
                                                  Class<?>[] paramTypes,
                                                  Object[] params) throws Exception {
        Class<?> clazz =  classLoader.loadClass(className);
        Method method = clazz.getMethod(methodName, paramTypes);
        Object value = null;
        if (isPublicStatic(method)) {
            value = method.invoke(null, params);
        }
        return value;
    }

    /**
     * 调用类定义的私有方法
     *
     * @param obj 调用类对象
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static Object invokeDeclaredMethod(Object obj,
                                      String methodName,
                                      Class<?>[] paramTypes,
                                      Object[] params) throws Exception {
        Class<?> cls = obj.getClass();
        Method method = cls.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(obj, params);
    }

    /**
     * 调用公共（包括父类）方法
     *
     * @param obj 调用类对象
     * @param methodName 方法名
     * @param paramTypes 参数类型
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static Object invokeMethod(Object obj,
                                              String methodName,
                                              Class<?>[] paramTypes,
                                              Object[] params) throws Exception {
        Class<?> cls = obj.getClass();
        Method method = cls.getMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(obj, params);
    }

    /**
     * 是否是公用静态方法
     *
     * @param member
     * @return
     */
    private static boolean isPublicStatic(Member member) {
        boolean isPS = false;
        int mod = member.getModifiers();
        isPS = Modifier.isPublic(mod) && Modifier.isStatic(mod);
        return isPS;
    }

}
