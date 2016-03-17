package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by SAMAR on 2/18/2016.
 */
public abstract class BaseAuthenticatedActivity extends BaseActivity {

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!application.getAuth().getUser().isLoggedIn()) {

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        onSocialCreate(savedInstanceState);
    }

    protected abstract void onSocialCreate(Bundle savedState);

}
