package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import java.security.PrivateKey;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Account;

/**
 * Created by samar_000 on 2/22/2016.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_EXTERNAL_PROVIDER = "EXTRA_EXTERNAL_PROVIDER";
    public static final String EXTRA_EXTERNAL_USERNAME = "EXTRA_EXTERNAL_USERNAME";
    public static final String EXTRA_EXTERNAL_TOKEN = "EXTRA_EXTERNAL_TOKEN";

    EditText passwordTxt, emailTxt, userNameTxt;
    Button registerBtn;
    View progressBar;
    private String defaultRegisterButtonText;


    private boolean isExternalLogin;
    private String externalToken;
    private String externalProvider;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameTxt = (EditText) findViewById(R.id.activity_register_userName);
        emailTxt = (EditText) findViewById(R.id.activity_register_email);
        passwordTxt = (EditText) findViewById(R.id.activity_register_password);
        registerBtn = (Button) findViewById(R.id.activity_register_btnRegister);
        progressBar = findViewById(R.id.activity_register_ProgressBar);


        registerBtn.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
        defaultRegisterButtonText = registerBtn.getText().toString();


        Intent intent = getIntent();
        externalToken = intent.getStringExtra(EXTRA_EXTERNAL_TOKEN);
        externalProvider = intent.getStringExtra(EXTRA_EXTERNAL_PROVIDER);
        isExternalLogin = externalToken != null;

    }

    @Override
    public void onClick(View v) {
        if (v == registerBtn) {

            progressBar.setVisibility(View.VISIBLE);
            registerBtn.setText("Loading...");
            registerBtn.setEnabled(false);
            userNameTxt.setEnabled(false);
            emailTxt.setEnabled(false);
            passwordTxt.setEnabled(false);


            if (isExternalLogin) {
                bus.post(new Account.RegisterWithExternalTokenRequest(
                        userNameTxt.getText().toString(),
                        emailTxt.getText().toString(),
                        externalProvider,
                        externalToken
                        ));
            } else {

                bus.post(new Account.RegisterRequest(
                        userNameTxt.getText().toString(),
                        emailTxt.getText().toString(),
                        passwordTxt.getText().toString()));
            }
        }
    }


    @Subscribe
    public void registerResponse(Account.RegisterResponse response) {
        onUserResponse(response);
    }


    @Subscribe
    public void registerResponse(Account.RegisterWithExternalTokenResponse response) {
        onUserResponse(response);
    }


    private void onUserResponse(Account.UserResponse response) {
        if (response.didSucceed()) {
            setResult(RESULT_OK);
            finish();
            return;
        }

        response.showErrorToast(this);
        userNameTxt.setError(response.getPropertyError("username"));
        emailTxt.setError(response.getPropertyError("email"));
        passwordTxt.setError(response.getPropertyError("password"));

        registerBtn.setEnabled(true);
        userNameTxt.setEnabled(true);
        emailTxt.setEnabled(true);
        passwordTxt.setEnabled(true);


        progressBar.setVisibility(View.GONE);
        registerBtn.setText(defaultRegisterButtonText);
    }
}
