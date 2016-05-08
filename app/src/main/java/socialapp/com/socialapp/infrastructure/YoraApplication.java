package socialapp.com.socialapp.infrastructure;

import android.app.Application;
import android.net.Uri;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.otto.Bus;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import socialapp.com.socialapp.Services.Module;


public class YoraApplication extends Application {

    public static final Uri API_ENDPOINT = Uri.parse("http://yora-playground.3dbuzz.com");
    public static final String STUDENT_TOKEN = "378a39794159458dbef0359b5b08510e";

    private Auth auth;
    private Bus bus;
    private Picasso authPicasso;

    public YoraApplication() {
        bus = new Bus();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        auth = new Auth(this);

        createAuthedPicasso();

        Module.register(this);

    }

    private void createAuthedPicasso() {
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer" + getAuth().getAuthToken())
                        .addHeader("X-Student", STUDENT_TOKEN)
                        .build();

                return chain.proceed(newRequest);
            }
        });


        authPicasso = new Picasso.Builder(this)
                .downloader(new OkHttpDownloader(client))
                .build();
    }

    public Picasso getAuthPicasso (){
        return authPicasso;
    }


    public Auth getAuth() {
        return auth;
    }

    public Bus getBus() {
        return bus;
    }
}
