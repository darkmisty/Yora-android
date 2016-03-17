package socialapp.com.socialapp.Activities;


import android.os.Bundle;

import socialapp.com.socialapp.AppViews.MainNavDrawer;
import socialapp.com.socialapp.R;

public class ContactsActivity extends BaseAuthenticatedActivity {


    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_contacts);
        setNavDrawer(new MainNavDrawer(this));

    }
}
