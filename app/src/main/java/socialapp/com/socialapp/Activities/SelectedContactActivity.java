package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Contacts;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.views.UserDetailAdapter;


public class SelectedContactActivity extends BaseAuthenticatedActivity implements AdapterView.OnItemClickListener {

    public static final String RESULT_CONTACT = "RESULT_CONTACT";
    private static final int REQUEST_ADD_FRIEND = 1;

    private UserDetailAdapter adapter;
    private View progressFrame;

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_select_contact);
        getSupportActionBar().setTitle("Select Contact");

        adapter = new UserDetailAdapter(this);
        ListView listView = (ListView) findViewById(R.id.activity_add_contact_contactListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        bus.post(new Contacts.GetContactRequest(true));

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        UserDetails userDetails = adapter.getItem(position);
        Intent intent = new Intent();
        intent.putExtra(RESULT_CONTACT, userDetails);
        setResult(RESULT_OK, intent);
        finish();

    }

    @Subscribe
    public void onContactsReceived(Contacts.GetContactResponse response) {
        response.showErrorToast(this);

        progressFrame.setVisibility(View.GONE);



        adapter.clear();
        adapter.addAll(response.Contacts);
        adapter.notifyDataSetChanged();
    }
}
