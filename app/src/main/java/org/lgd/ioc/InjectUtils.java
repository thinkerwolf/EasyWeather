package org.lgd.ioc;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import org.lgd.ioc.annonation.EventBase;
import org.lgd.ioc.annonation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 注解工具类
 * 可以注解空间
 * 可以注解方法事件
 * Created by 凯 on 2016/2/17.
 */
public class InjectUtils {


    private static final String FIND_VIEW_BY_ID = "findViewById";

    /**
     * 注入点击事件
     *
     * @param v
     */
    public static void injectEvents(ViewGroup v) {
        Class clazz = v.getClass();
        Log.i("TAG", clazz.getName());
        Method[] methods = clazz.getMethods();
        //遍历所有的方法
        for (Method method : methods) {
            //拿到方法上所有的注解
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {

                Class<? extends Annotation> annotationType = annotation.annotationType();

                //拿到注解上的注解
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);

                if (eventBase != null) {
                    //取出监听器的名称，类型，调用方法名
                    String listenerSetter = eventBase.listenerSetter();
                    Class<?> listenerType = eventBase.listenerType();
                    String methodName = eventBase.methodName();

                    try {
                        //获取注解类中定义的方法
                        Method aMethod = annotationType.getDeclaredMethod("value");
                        Object obj = aMethod.invoke(annotation);
                        int[] viewIds;
                        if (obj instanceof int[]) {
                            viewIds = (int[]) obj;
                        } else {
                            return;
                        }
                        DynamicHandler handler = new DynamicHandler(v);
                        handler.addMethod(methodName, method);

                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class<?>[]{listenerType}, handler); //动态代理机制

                        for (int viewId : viewIds) {
                            View view = v.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                            setEventListenerMethod.invoke(view, listener);
                        }


                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 注册事件
     *
     * @param activity
     */
    public static void injectEvents(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Log.i("TAG", clazz.getName());
        Method[] methods = clazz.getMethods();
        //遍历所有的方法
        for (Method method : methods) {
            //拿到方法上所有的注解
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {

                Class<? extends Annotation> annotationType = annotation.annotationType();

                //拿到注解上的注解
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);

                if (eventBase != null) {
                    //取出监听器的名称，类型，调用方法名
                    String listenerSetter = eventBase.listenerSetter();
                    Class<?> listenerType = eventBase.listenerType();
                    String methodName = eventBase.methodName();

                    try {
                        //获取注解类中定义的方法
                        Method aMethod = annotationType.getDeclaredMethod("value");
                        Object obj = aMethod.invoke(annotation);
                        int[] viewIds;
                        if (obj instanceof int[]) {
                            viewIds = (int[]) obj;
                        } else {
                            return;
                        }
                        DynamicHandler handler = new DynamicHandler(activity);
                        handler.addMethod(methodName, method);

                        Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class<?>[]{listenerType}, handler); //动态代理机制

                        for (int viewId : viewIds) {
                            View view = activity.findViewById(viewId);
                            Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                            setEventListenerMethod.invoke(view, listener);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 控件视图注入
     *
     * @param activity
     */
    public static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.value();
                if (viewId != -1)
                    try {
                        Method method = clazz.getMethod(FIND_VIEW_BY_ID, int.class);
                        Object res = method.invoke(activity, viewId);
                        field.setAccessible(true);
                        field.set(activity, res);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }

    }

    /**
     * 注入视图
     *
     * @param f
     */
    public static void injectView(Fragment f, View v) {
        Class clazz = f.getClass();
        Class clavv = v.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.value();
                if (viewId != -1) {
                    try {
                        Method method = clavv.getMethod(FIND_VIEW_BY_ID, int.class);
                        Object obj = method.invoke(v, viewId);
                        field.setAccessible(true);
                        field.set(f, obj);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

}
