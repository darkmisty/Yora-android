package socialapp.com.socialapp.infrastructure;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SAMAR on 4/11/2016.
 */
public class ActionScheduler {

    private final MyApplication application;
    private final Handler handler;
    private final ArrayList<TimedCallback> timedCallBacks;
    private final HashMap<Class, Runnable> onResumeAction;
    private boolean isPaused;

    public ActionScheduler(MyApplication application) {
        this.application = application;
        handler = new Handler();
        timedCallBacks = new ArrayList<>();
        onResumeAction = new HashMap<>();
        isPaused = false;

    }

    public void onPause() {
        isPaused = true;
    }

    public void onResume() {
        isPaused = false;

        for (TimedCallback callback : timedCallBacks) {
            callback.schedule();
        }

        for (Runnable runnable : onResumeAction.values()) {
            runnable.run();
        }

        onResumeAction.clear();
    }

    public void invokeOnResume(Class cls, Runnable runnable) {

        if (!isPaused) {
            runnable.run();
            return;
        }

        onResumeAction.put(cls, runnable);

    }

    public void postDelayed(Runnable runnable, Long milliseconds) {

        handler.postDelayed(runnable, milliseconds);


    }


    public void invokeEveryMillisecond(Runnable runnable, long milliseconds) {

        invokeEveryMillisecond(runnable, milliseconds, true);

    }


    public void invokeEveryMillisecond(Runnable runnable, long millisecond, boolean runImmediatly) {

        TimedCallback callback = new TimedCallback(runnable, millisecond);
        timedCallBacks.add(callback);

        if (runImmediatly) {
            callback.run();
        } else {
            postDelayed(callback, millisecond);
        }


    }

    public void postEveryMilliseconds(Object request, long millisecond) {

        postEveryMilliseconds(request, millisecond, true);
    }

    public void postEveryMilliseconds(final Object request, long millisecond, boolean postImmedialty) {
        invokeEveryMillisecond(new Runnable() {
            @Override
            public void run() {
                application.getBus().post(request);
            }
        }, millisecond);
    }

    private class TimedCallback implements Runnable {

        private final Runnable runnable;
        private final long delay;

        public TimedCallback(Runnable runnable, long delay) {
            this.runnable = runnable;
            this.delay = delay;
        }

        @Override
        public void run() {

            if (isPaused)
                return;

            runnable.run();
            schedule();

        }

        public void schedule() {
            handler.postDelayed(this, delay);
        }
    }
}
