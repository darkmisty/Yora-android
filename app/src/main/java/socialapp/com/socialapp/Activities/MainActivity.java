package socialapp.com.socialapp.Activities;

import android.os.Bundle;

import socialapp.com.socialapp.AppViews.MainNavDrawer;
import socialapp.com.socialapp.R;

/**
 * Created by SAMAR on 2/18/2016.
 */


public class MainActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inbox");
        setNavDrawer(new MainNavDrawer(this));
    }


}
