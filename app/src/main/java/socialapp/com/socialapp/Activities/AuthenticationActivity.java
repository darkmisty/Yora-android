package socialapp.com.socialapp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Account;
import socialapp.com.socialapp.Utilities.Utils;
import socialapp.com.socialapp.infrastructure.Auth;

public class AuthenticationActivity extends BaseActivity {

    public static final String EXTRA_RETURN_TO_ACTIVITY = "extraReturnToActivity";
    private Auth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Log.e("Authentication Activity", "LUNCHED");

        auth = application.getAuth();

        if (!auth.hasAuthToken()) {

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bus.post(new Account.LoginWithLocalTokenRequest(auth.getAuthToken()));


    }

    @Subscribe
    public void onLoginWithLocalTokens(Account.LoginWithLocalTokenResponse response) {

        if (!response.didSucceed()) {

            Utils.tmsg(this, "Please Login again");
            auth.setAuthToke(null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }


        Intent intent;
        String returnTo = getIntent().getStringExtra(EXTRA_RETURN_TO_ACTIVITY);
        if (returnTo != null) {

            try {
                intent = new Intent(this, Class.forName(returnTo));
            } catch (Exception ignore) {
                intent = new Intent(this, MainActivity.class);
            }
        } else {

            intent = new Intent(this, MainActivity.class);
        }

        startActivity(intent);
        finish();


    }
}
