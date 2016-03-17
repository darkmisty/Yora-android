package socialapp.com.socialapp.Utilities;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by SAMAR on 2/18/2016.
 */
public class HelperClass {

    Activity activity;

    public HelperClass(Activity activity) {
        this.activity = activity;
    }

    public void tmsg(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }
}
