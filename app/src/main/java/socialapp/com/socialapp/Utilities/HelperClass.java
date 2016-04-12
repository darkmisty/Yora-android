package socialapp.com.socialapp.Utilities;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by SAMAR on 2/18/2016.
 */
public class HelperClass {

    public static void tmsg(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
