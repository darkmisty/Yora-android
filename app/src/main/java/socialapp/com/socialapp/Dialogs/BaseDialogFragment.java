package socialapp.com.socialapp.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.squareup.otto.Bus;

import socialapp.com.socialapp.infrastructure.SocialApplication;


public class BaseDialogFragment extends DialogFragment {

    protected SocialApplication application;
    protected Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (SocialApplication) getActivity().getApplication();

        bus = application.getBus();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        bus.unregister(this);
    }
}
