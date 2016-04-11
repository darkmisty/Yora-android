package socialapp.com.socialapp.Activities;

import android.os.Bundle;

import socialapp.com.socialapp.R;

/**
 * Created by SAMAR on 4/11/2016.
 */
public class ContactActivity extends BaseAuthenticatedActivity {

    public static final String EXTRA_USER_DETAILS = "EXTRA_USER_DETAILS";

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_contact);
    }


}
