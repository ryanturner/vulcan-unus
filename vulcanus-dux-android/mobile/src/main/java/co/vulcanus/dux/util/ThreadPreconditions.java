package co.vulcanus.dux.util;

import android.os.Looper;

import co.vulcanus.dux.BuildConfig;

/**
 * Created by ryan_turner on 11/11/15.
 */
public class ThreadPreconditions {
    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("This method should be called from the Main Thread");
            }
        }
    }
}
