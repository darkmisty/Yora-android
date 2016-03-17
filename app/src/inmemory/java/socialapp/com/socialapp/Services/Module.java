package socialapp.com.socialapp.Services;


import android.util.Log;

import socialapp.com.socialapp.infrastructure.SocialApplication;

public class Module {

    public static void register(SocialApplication application) {

        Log.e("Module", "IN MEMORY Register Method Called");
        new InMemoryAccountService(application);
    }
}