package socialapp.com.socialapp.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.Activities.BaseActivity;
import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Contacts;
import socialapp.com.socialapp.views.ContactRequestAdapter;

/**
 * Created by SAMAR on 4/11/2016.
 */
public class PendingContactRequestsFragment extends BaseFragment {

    private View progressFrame;
    private ContactRequestAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pending_contacts_request, container, false);

        progressFrame = v.findViewById(R.id.fragment_pending_contact_requests_progressFrame);
        adapter = new ContactRequestAdapter(((BaseActivity) getActivity()));

        ListView listView = (ListView) v.findViewById(R.id.fragment_pending_contact_requests_list);
        listView.setAdapter(adapter);

        bus.post(new Contacts.GetContactRequestsRequest(true));

        return v;
    }


    @Subscribe
    public void onGetCotnactRequest(final Contacts.GetContactRequestResponse response) {


        scheduler.invokeOnResume(Contacts.GetContactRequestResponse.class, new Runnable() {
            @Override
            public void run() {
                progressFrame.animate().alpha(0).setDuration(250).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        progressFrame.animate()
                                .alpha(0)
                                .setDuration(250)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressFrame.setVisibility(View.GONE);
                                    }
                                });
                    }
                }).start();

                if (!response.didSucceed()) {
                    response.showErrorToast(getActivity());
                    return;
                }

                adapter.clear();
                adapter.addAll(response.Request);
            }
        });


    }
}
