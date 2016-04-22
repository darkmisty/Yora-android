package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private static final int REQUEST_ADD_CONTACT = 1;

    private UserDetailAdapter adapter;

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

    @Subscribe
    public void onContactsReceived(final Contacts.GetContactResponse response) {


        scheduler.invokeOnResume(Contacts.GetContactResponse.class, new Runnable() {
            @Override
            public void run() {
                response.showErrorToast(SelectedContactActivity.this);
                adapter.clear();
                adapter.addAll(response.Contacts);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select_contact, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.activity_select_contact_menuAddContact) {
            startActivityForResult(new Intent(this, AddContactActivity.class), REQUEST_ADD_CONTACT);
            return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_CONTACT && resultCode == RESULT_OK) {
            UserDetails user = data.getParcelableExtra(AddContactActivity.RESULT_CONTACT);
            selectUser(user);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        selectUser(adapter.getItem(position));


    }

    private void selectUser(UserDetails user) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_CONTACT, user);
        setResult(RESULT_OK, intent);
        finish();
    }
}
