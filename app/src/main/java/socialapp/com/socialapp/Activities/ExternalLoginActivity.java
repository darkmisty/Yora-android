package socialapp.com.socialapp.Activities;


import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import socialapp.com.socialapp.R;

public class ExternalLoginActivity extends BaseActivity implements View.OnClickListener {


    public static final String EXTRA_EXTERNAL_SERVICE = "EXTRA_EXTERNAL_SERVICE";
    private Button textBtn;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_login);

        textBtn = (Button) findViewById(R.id.activity_external_login_testButton);
        webView = (WebView) findViewById(R.id.activity_external_login_webView);

        textBtn.setOnClickListener(this);
        textBtn.setText("Log In With: " + getIntent().getStringExtra(EXTRA_EXTERNAL_SERVICE));

    }

    @Override
    public void onClick(View v) {
        if (v == textBtn) {
            application.getAuth().getUser().setLoggedIn(true);
            setResult(RESULT_OK);
            finish();
        }
    }
}
