package socialapp.com.socialapp.infrastructure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import socialapp.com.socialapp.Activities.LoginActivity;


public class Auth {

    private static final String AUTH_PREFERENCES = "AUTH_PREFERENCES";
    private static final String AUTH_PREFERENCES_TOKEN = "AUTH_PREFERENCES_TOKEN";


    private final Context context;
    private final SharedPreferences preferences;

    private User user;
    private String authToken;

    public Auth(Context context) {
        this.context = context;
        user = new User();
        preferences = context.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE);
        authToken = preferences.getString(AUTH_PREFERENCES_TOKEN, null);
    }

    public User getUser() {
        return user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public boolean hasAuthToken() {
        return authToken != null && !authToken.isEmpty();
    }

    public void setAuthToke(String authToken) {
        this.authToken = authToken;

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTH_PREFERENCES_TOKEN, authToken);
        editor.commit();
    }

    public void logout() {
        //Remove the authtoken
        setAuthToke(null);

        Intent loginIntent = new Intent(context, LoginActivity.class);
        //Clears out the entire application and restart it again
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(loginIntent);
    }
}
