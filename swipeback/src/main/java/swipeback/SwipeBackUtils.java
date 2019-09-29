
package swipeback;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Chaojun Wang on 6/9/14.
 */
public class SwipeBackUtils {
    private SwipeBackUtils() {
    }

    public static void log(Object obj) {
        Log.i("swipeback", obj.toString());
    }

    public static void printStackTrace(Throwable e) {
        e.printStackTrace();
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable e) {
            SwipeBackUtils.printStackTrace(e);
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(android.app.Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity, PageTranslucentListener listener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity, listener);
        } else {
            convertActivityToTranslucentBeforeL(activity, listener);
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms before Android 5.0
     */
    private static void convertActivityToTranslucentBeforeL(Activity activity, PageTranslucentListener listener) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            MyInvocationHandler invocationHandler = new MyInvocationHandler(new WeakReference<>(listener));
            Object obj = Proxy.newProxyInstance(Activity.class.getClassLoader(), new Class[]{translucentConversionListenerClazz}, invocationHandler);

            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, obj);
        } catch (Throwable e) {
            SwipeBackUtils.printStackTrace(e);
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private static void convertActivityToTranslucentAfterL(Activity activity, PageTranslucentListener listener) {
        try {
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            MyInvocationHandler invocationHandler = new MyInvocationHandler(new WeakReference<>(listener));
            Object obj = Proxy.newProxyInstance(Activity.class.getClassLoader(), new Class[]{translucentConversionListenerClazz}, invocationHandler);

            SwipeBackUtils.log("convertToTranslucent: start time: " + System.currentTimeMillis());
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent", translucentConversionListenerClazz, ActivityOptions.class);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, obj, options);
        } catch (Throwable e) {
            SwipeBackUtils.printStackTrace(e);
        }
    }

    public interface PageTranslucentListener {
        void onPageTranslucent();
    }

    static class MyInvocationHandler implements InvocationHandler {
        private WeakReference<PageTranslucentListener> listener;

        public MyInvocationHandler(WeakReference<PageTranslucentListener> listener) {
            this.listener = listener;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            SwipeBackUtils.log("convertToTranslucent: end time: " + System.currentTimeMillis());
            try {
                boolean success = (boolean) args[0];
                PageTranslucentListener listener = this.listener.get();
                if (success && listener != null) {
                    listener.onPageTranslucent();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
