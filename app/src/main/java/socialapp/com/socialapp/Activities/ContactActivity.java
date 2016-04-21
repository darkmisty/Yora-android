package socialapp.com.socialapp.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Contacts;
import socialapp.com.socialapp.Services.Messages;
import socialapp.com.socialapp.Services.entities.Message;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.views.MessagesAdapter;

/**
 * Created by SAMAR on 4/11/2016.
 */

public class ContactActivity extends BaseAuthenticatedActivity implements MessagesAdapter.onMessageClickedListener {

    public static final String EXTRA_USER_DETAILS = "EXTRA_USER_DETAILS";
    private static final int RESULT_USER_REMOVED = 101;


    private UserDetails userDetails;
    private MessagesAdapter adapter;
    private ArrayList<Message> messages;
    private View progressFrame;

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_contact);


        userDetails = getIntent().getParcelableExtra(EXTRA_USER_DETAILS);

        if (userDetails == null) {
            userDetails = new UserDetails(1, true, "Lorem Ipsum", "lorem123", "http://www.gravatar.com/avatar/1.jpg");
        }

        getSupportActionBar().setTitle(userDetails.getDisplayName());
        toolbar.setNavigationIcon(R.drawable.ic_ab_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new MessagesAdapter(this, this);
        messages = adapter.getMessages();

        progressFrame = findViewById(R.id.activity_contact_progressFrame);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_contact_messages);

        if (isTablet) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }


        recyclerView.setAdapter(adapter);


        scheduler.postEveryMilliseconds(new Messages.SearchMessagesRequest(userDetails.getId(), true, true), 1000 * 60 * 3);
    }


    @Override
    public void onMessageClicked(Message message) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(MessageActivity.EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Subscribe
    public void onMessagesReceived(final Messages.SearchMessagesResponse response) {
        scheduler.invokeOnResume(Messages.SearchMessagesResponse.class, new Runnable() {
            @Override
            public void run() {
                if (!response.didSucceed()) {
                    response.showErrorToast(ContactActivity.this);
                    return;
                }

                int oldSize = messages.size();
                messages.clear();
                adapter.notifyItemRangeRemoved(0, oldSize);

                messages.addAll(response.Message);
                adapter.notifyItemRangeInserted(0, messages.size());

                progressFrame.setVisibility(View.GONE);

            }
        });
    }

    private void doRemoveContact() {

        progressFrame.setVisibility(View.VISIBLE);
        bus.post(new Contacts.RemoveContactRequest(userDetails.getId()));
    }

    @Subscribe
    public void onRemoveContact(final Contacts.RemoveContactResponse response) {
        scheduler.invokeOnResume(Contacts.RemoveContactRequest.class, new Runnable() {
            @Override
            public void run() {

                if (!response.didSucceed()) {
                    response.showErrorToast(ContactActivity.this);
                    progressFrame.setVisibility(View.VISIBLE);
                    return;
                }
                setResult(RESULT_USER_REMOVED);

                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_contact, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.activity_contact_menu_new_message) {
            Intent intent = new Intent(this, NewMessageActivity.class);
            intent.putExtra(NewMessageActivity.EXTRA_CONTACT, userDetails);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.activity_contact_menu_remove_friend) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Remove Friend")
                    .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doRemoveContact();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create();

            dialog.show();
            return true;
        }

        return false;
    }
}
