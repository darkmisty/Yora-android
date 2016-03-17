package socialapp.com.socialapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import socialapp.com.socialapp.Fragments.LoginFragment;
import socialapp.com.socialapp.R;

/**
 * Created by SAMAR on 2/20/2016.
 */
public class LoginNarrowActivity extends BaseActivity implements LoginFragment.CallBacks{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_narrow);
    }

    @Override
    public void onLoggedIn() {
        setResult(RESULT_OK);
        finish();
    }
}
