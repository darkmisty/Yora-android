package socialapp.com.socialapp.Services;


import android.util.Log;

import socialapp.com.socialapp.infrastructure.MyApplication;

public class Module {

    public static void register(MyApplication application) {

        Log.e("Module", "IN MEMORY Register Method Called");
        new InMemoryAccountService(application);
    }
}