package socialapp.com.socialapp.Activities;

import android.os.Bundle;

import socialapp.com.socialapp.AppViews.MainNavDrawer;
import socialapp.com.socialapp.R;

/**
 * Created by SAMAR on 3/1/2016.
 */
public class SendMessagesActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_sent_messages);
        setNavDrawer(new MainNavDrawer(this));
    }
}
