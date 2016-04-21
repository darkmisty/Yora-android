package socialapp.com.socialapp.Activities;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;

import com.squareup.otto.Bus;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.infrastructure.ActionScheduler;
import socialapp.com.socialapp.infrastructure.MyApplication;
import socialapp.com.socialapp.views.NavDrawer;


public abstract class BaseActivity extends AppCompatActivity {

    protected MyApplication application;
    protected Bus bus;
    protected Toolbar toolbar;
    protected NavDrawer navDrawer;
    protected boolean isTablet;
    protected ActionScheduler scheduler;
    private boolean isRegisterWithBus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        application = (MyApplication) getApplication();
        bus = application.getBus();
        scheduler = new ActionScheduler(application);


        DisplayMetrics metrics = getResources().getDisplayMetrics();

        /*check if app is running on tablet or phone
        in this case if its more or equal to 600 (which mean tablet)
        then isTablet will be true, otherwise it will false
        * */
        isTablet = (metrics.widthPixels / metrics.density) >= 600;

        bus.register(this);
        isRegisterWithBus = true;
    }


    public ActionScheduler getScheduler() {
        return scheduler;
    }


    @Override
    protected void onResume() {
        super.onResume();

        scheduler.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        scheduler.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isRegisterWithBus) {
            bus.unregister(this);
            isRegisterWithBus = false;
        }

        if (navDrawer != null)
            navDrawer.destroy();

    }

    @Override
    public void finish() {
        super.finish();

        if (isRegisterWithBus) {
            bus.unregister(this);
            isRegisterWithBus = false;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        toolbar = (Toolbar) findViewById(R.id.include_toolbar);

        if (toolbar != null)
            setSupportActionBar(toolbar);

    }

    public void fadeOut(final FadeOutListener listener) {

        View rootView = findViewById(android.R.id.content);

        rootView.animate()
                .alpha(0)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onFadeOutEnd();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {


                    }
                })
                .setDuration(300)
                .start();

    }

    protected void setNavDrawer(NavDrawer drawer) {
        this.navDrawer = drawer;
        this.navDrawer.create();


        //Disable default animation when switching from one activitiy from another
        overridePendingTransition(0, 0);

        View rootView = findViewById(android.R.id.content);
        rootView.setAlpha(0);

        rootView.animate()
                .alpha(1)
                .setDuration(450)
                .start();

    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public MyApplication getMyApplication() {
        return application;
    }


    public interface FadeOutListener {
        void onFadeOutEnd();
    }


}
