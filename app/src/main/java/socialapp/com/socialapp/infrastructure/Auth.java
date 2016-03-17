package socialapp.com.socialapp.infrastructure;

import android.content.Context;

/**
 * Created by SAMAR on 2/18/2016.
 */
public class Auth {
    private final Context context;
    private User user;

    public Auth(Context context) {
        this.context = context;
        user = new User();
    }

    public User getUser() {
        return user;
    }
}
