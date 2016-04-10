package socialapp.com.socialapp.Services;

import android.os.Handler;

import com.squareup.otto.Bus;

import java.util.Random;

import socialapp.com.socialapp.infrastructure.MyApplication;

/**
 * Created by SAMAR on 3/7/2016.
 */
public abstract class BaseInMemoryService {

    protected final Bus bus;
    protected MyApplication application;
    protected Handler handler;
    protected Random random;

    protected BaseInMemoryService(MyApplication application) {
        this.application = application;
        bus = application.getBus();
        handler = new Handler();
        random = new Random();
        bus.register(this);
    }


    protected void invokeDelayed(Runnable runnable, long milliSecondMin, long milliSecondMax) {

        if (milliSecondMin > milliSecondMax)
            throw new IllegalArgumentException("Min should be smaller than max");

        long delay = ((long) random.nextDouble() * (milliSecondMax - milliSecondMin) + milliSecondMin);

        handler.postDelayed(runnable, delay);


    }

    protected void postDelayed(final Object event, long milliSecondMin, long milliSecondMax) {

        invokeDelayed(new Runnable() {
            @Override
            public void run() {

                bus.post(event);

            }
        }, milliSecondMin, milliSecondMax);
    }

    protected void postDelayed(Object event, long millisecond) {

        postDelayed(event, millisecond, millisecond);
    }

    protected void postDelayed(Object event) {
        postDelayed(event, 600, 1200);
    }


}
