package socialapp.com.socialapp.Services;

import com.squareup.otto.Bus;

import socialapp.com.socialapp.infrastructure.YoraApplication;

/**
 * Created by SAMAR on 5/3/2016.
 */
public class BaseLiveService {
    protected Bus bus;
    protected final YoraWebService api;
    protected final YoraApplication application;

    public BaseLiveService(YoraWebService api, YoraApplication application) {
        this.api = api;
        this.application = application;
        this.bus = application.getBus();
        bus.register(this);
    }
}
