package socialapp.com.socialapp.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import socialapp.com.socialapp.R;

/**
 * Created by samar_000 on 2/22/2016.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    EditText passwordTxt, emailTxt, userNameTxt;
    Button registerBtn;
    View progressBar;

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

    }

    @Override
    public void onClick(View v) {
        if (v == registerBtn) {
            application.getAuth().getUser().setLoggedIn(true);
            setResult(RESULT_OK);
            finish();
        }
    }
}
