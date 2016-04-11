package socialapp.com.socialapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import socialapp.com.socialapp.R;

/**
 * Created by SAMAR on 4/11/2016.
 */
public class PendingContactRequestsFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending_contacts_request, container, false);

        return v;
    }
}
