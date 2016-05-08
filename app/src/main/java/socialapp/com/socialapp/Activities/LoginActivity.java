package socialapp.com.socialapp.Activities;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import socialapp.com.socialapp.Fragments.LoginFragment;
import socialapp.com.socialapp.Fragments.RegisterGcmFragment;
import socialapp.com.socialapp.R;


/**
 * Created by SAMAR on 2/18/2016.
 */


public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginFragment.CallBacks {


    private static final int REQUEST_NARROW_LOGIN = 1;
    private static final int REQUEST_REGISTER = 2;
    private static final int REQUEST_EXTERNAL_LOGIN = 3;
    private View loginBtn, registerBtn, facebookLoginBtn, googleLoginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);


        loginBtn = findViewById(R.id.login_activity_login);
        registerBtn = findViewById(R.id.login_activity_register);
        facebookLoginBtn = findViewById(R.id.login_activity_facebook);
        googleLoginBtn = findViewById(R.id.login_activity_google);

        if (loginBtn != null) {
            loginBtn.setOnClickListener(this);
        }

        registerBtn.setOnClickListener(this);
        facebookLoginBtn.setOnClickListener(this);
        googleLoginBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == loginBtn)
            startActivityForResult(new Intent(this, LoginNarrowActivity.class), REQUEST_NARROW_LOGIN);
        else if (v == registerBtn)
            startActivityForResult(new Intent(this, RegisterActivity.class), REQUEST_REGISTER);
        else if (v == facebookLoginBtn)
            doExternalLogin("Facebook");
        else if (v == googleLoginBtn)
            doExternalLogin("Google");


    }

    private void doExternalLogin(String externalProvider) {
        Intent intent = new Intent(this, ExternalLoginActivity.class);
        intent.putExtra(ExternalLoginActivity.EXTRA_EXTERNAL_SERVICE, externalProvider);
        startActivityForResult(intent, REQUEST_EXTERNAL_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_NARROW_LOGIN ||
                requestCode == REQUEST_REGISTER ||
                requestCode == REQUEST_EXTERNAL_LOGIN)
            RegisterGcmFragment.get(new RegisterGcmFragment.GcmRegistrationCallback() {
                @Override
                public void gcmFinished() {
                    finishLogin();
                }
            }, requestCode == REQUEST_REGISTER , getFragmentManager());
    }

    private void finishLogin() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoggedIn() {
         finishLogin();
    }
}
