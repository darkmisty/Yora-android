package socialapp.com.socialapp.Activities;

import android.os.Bundle;

import socialapp.com.socialapp.R;

/**
 * Created by SAMAR on 4/11/2016.
 */
public class AddContactActivity extends BaseAuthenticatedActivity {


    public static final String RESULT_CONTACT = "RESULT_CONTACT";

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_add_contact);
    }
}
