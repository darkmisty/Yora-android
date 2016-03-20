package socialapp.com.socialapp.Fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.squareup.otto.Bus;

import socialapp.com.socialapp.infrastructure.MyApplication;

/**
 * Created by SAMAR on 2/20/2016.
 */
public abstract class BaseFragment extends Fragment {

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
