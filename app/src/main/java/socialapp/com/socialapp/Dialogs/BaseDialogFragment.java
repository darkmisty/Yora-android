package socialapp.com.socialapp.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;

import com.squareup.otto.Bus;

import socialapp.com.socialapp.infrastructure.MyApplication;


public class BaseDialogFragment extends DialogFragment {

    protected MyApplication application;
    protected Bus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();

        bus = application.getBus();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        bus.unregister(this);
    }
}
