package socialapp.com.socialapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.Activities.AddContactActivity;
import socialapp.com.socialapp.Activities.BaseActivity;
import socialapp.com.socialapp.Activities.ContactActivity;
import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Contacts;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.views.UserDetailAdapter;

/**
 * Created by SAMAR on 4/11/2016.
 */
public class ContactsFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private UserDetailAdapter adapter;
    private View progressFrame;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);
        setHasOptionsMenu(true);

        adapter = new UserDetailAdapter((BaseActivity) getActivity());
        progressFrame = v.findViewById(R.id.fragment_contacts_progressFrame);

        ListView listView = (ListView) v.findViewById(R.id.fragment_contacts_list);
        listView.setEmptyView(v.findViewById(R.id.fragment_contacts_emptyList));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Contacts.GetContactRequest(false));

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserDetails detail = adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ContactActivity.class);
        intent.putExtra(ContactActivity.EXTRA_USER_DETAILS, detail);
        startActivity(intent);

    }

    @Subscribe
    public void onContactResponse(final Contacts.GetContactResponse response) {
        scheduler.invokeOnResume(Contacts.GetContactResponse.class, new Runnable() {
            @Override
            public void run() {
                response.showErrorToast(getActivity());

                progressFrame.animate()
                        .alpha(0)
                        .setDuration(250)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                progressFrame.setVisibility(View.GONE);
                            }
                        })
                        .start();

                adapter.clear();
                adapter.addAll(response.Contacts);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_contacts, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.fragment_contacts_menu_addContact) {
            startActivity(new Intent(getActivity(), AddContactActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
