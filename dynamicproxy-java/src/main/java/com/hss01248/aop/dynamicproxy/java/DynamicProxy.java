package com.hss01248.aop.dynamicproxy.java;



import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class DynamicProxy {

    public static boolean enable = true;
    public static String TAG = "aspectImpl";


    public static <T> T getProxy(T impl) {
        return getProxy(impl, true, true,  true, null);

    }

    /**
     * 打印接口的方法; 示例:
     * 正常:              PGReport@1a5b9ed.enterV4(actionname, 300134, null, false), result:null, cost:6ms ,thread:main
     * 方法执行发生错误时:  PGReport@f1ebd59.enter(actionname, 300134, false), throw ArithmeticException:divide by zero, cost:5ms ,thread:main
     * <p>
     * 日志tag: aspectImpl
     * <p>
     * impl.getClass().getInterfaces()只能拿到当前类实现的接口,拿不到父类实现的接口,所以需要层层剥离
     *
     * @param impl
     * @param <T>
     * @return
     */
    public static <T> T getProxy(T impl, boolean enableProxyThis, boolean enableLog,
                                 boolean safeCatchException, ProxyCallback callback) {
        if (!enable) {
            System.out.println( "disable dynamic proxy by global config:" + impl);
            return impl;
        }
        if (!enableProxyThis) {
            w(TAG, "disable dynamic proxy by current config:" + impl);
            return impl;
        }
        Class<?>[] classes = getInterfaces2(impl);

        if (classes == null || classes.length == 0) {
           w(TAG, impl.getClass().getName() + " : no interfaces");
            return impl;
        }
        v(TAG, impl.getClass().getName() + " : getInterfaces : " + Arrays.toString(classes));
        long start0 = System.currentTimeMillis();
        Object obj = Proxy.newProxyInstance(impl.getClass().getClassLoader(), classes, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                StringBuilder sb = new StringBuilder();
                String parms = Arrays.toString(args);
                parms = parms.substring(1, parms.length() - 1);

                String objName = impl + "";
                if (objName.contains(".")) {
                    objName = objName.substring(objName.lastIndexOf(".") + 1);
                }
                sb.append(objName)
                        .append(".")
                        .append(method.getName())
                        .append("(")
                        .append(parms)
                        .append(")");

                if (callback != null) {
                    callback.before(proxy, method, args);
                }

                boolean isReturnVoid = Void.TYPE.equals(method.getReturnType());

                return reallyInvoke(enableLog, method, impl, args, sb, start0, callback, proxy, safeCatchException);
            }
        });
        try {
            T t = (T) obj;
            return t;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return impl;
        }

    }



    private static <T> Object reallyInvoke(boolean enableLog, Method method, T impl, Object[] args, StringBuilder sb,
                                           long start0, ProxyCallback callback, Object proxy, boolean safeCatchException) throws Throwable {
        Object obj = null;
        long start = System.currentTimeMillis();
        try {
            obj = method.invoke(impl, args);
            sb.append(", result:")
                    .append(obj);
            doLog(enableLog, sb, start, start0);
            if (callback != null) {
                callback.onResult(proxy, obj, method, args,System.currentTimeMillis() - start);
            }
        } catch (Throwable throwable) {
            sb.append(EXCEPTION_DESC)
                    .append(throwable.getClass().getName())
                    .append(":")
                    .append(throwable.getMessage());
            doLog(enableLog, sb, start, start0);
            if (callback != null) {
                callback.onException(proxy, throwable, method, args,System.currentTimeMillis() - start);
            }
            if (safeCatchException) {
                if (enableLog) {
                    throwable.printStackTrace();
                }
            } else {
                throw throwable;
            }

        }
        return obj;

    }

    private static <T> Class<?>[] getInterfaces2(T impl) {
        Set<Class> classes = getInterfaces(impl.getClass(), null);
        if (classes.size() > 0) {
            Class[] classes1 = new Class[classes.size()];
            Iterator<Class> iterator = classes.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                classes1[i] = iterator.next();
                i++;
            }
            return classes1;
        }
        return null;
    }

    private static <T> Set<Class> getInterfaces(Class clazz, Set<Class> classes) {
        if (classes == null) {
            classes = new HashSet<>();
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {

        } else {
            for (Class<?> anInterface : interfaces) {
                classes.add(anInterface);
                //接口的父类
                Class superInter = anInterface.getSuperclass();
                if (superInter != null) {
                    classes.addAll(getInterfaces(superInter, classes));
                }
            }
        }
        //类的父类
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            classes.addAll(getInterfaces(superClazz, classes));
        }
        return classes;
    }

    private static void doLog(boolean enableLog, StringBuilder sb0, long start, long start0) {
        if (!enableLog) {
            return;
        }
        long cost = System.currentTimeMillis() - start;
        long costFromObjInit = System.currentTimeMillis() - start0;
        StringBuilder sb = new StringBuilder();
        sb.append("method cost:")
                .append(cost).append("ms")
                .append(", costFromObjInit :")
                .append(costFromObjInit).append("ms")
                .append(" ,thread:").append(Thread.currentThread().getName()).append("\n");


        String str = sb.toString();
        sb0.insert(0, str);
        str = sb0.toString();

        if ("main".equals(Thread.currentThread().getName()) && cost > 50) {
            i(TAG, str);
        } else if (str.contains(EXCEPTION_DESC)) {
           w(TAG, str);
        } else {
           d(TAG, str);
        }
    }

    private static void d(String tag, String str) {
        System.out.println(tag+"-"+str);
    }

    private static void i(String tag, String str) {
        System.out.println(tag+"-"+str);
    }
    private static void v(String tag, String str) {
        System.out.println(tag+"-"+str);
    }

    private static void w(String tag, String str) {
        System.out.println(tag+"-"+str);
    }

    static String EXCEPTION_DESC = ", throw ";

    public interface ProxyCallback {

        default void before(Object proxy, Method method, Object[] args) {
        }

        default void executeByOtherThread(Runnable runnable){
        }

        default void onResult(Object proxy, Object result, Method method, Object[] args,long cost) {
        }

        default void onException(Object proxy, Throwable result, Method method, Object[] args,long cost) {
        }

    }
}
