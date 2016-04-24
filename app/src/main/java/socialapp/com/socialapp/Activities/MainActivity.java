package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.util.List;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Contacts;
import socialapp.com.socialapp.Services.Messages;
import socialapp.com.socialapp.Services.entities.ContactRequest;
import socialapp.com.socialapp.Services.entities.Message;
import socialapp.com.socialapp.views.MainActivityAdapter;
import socialapp.com.socialapp.views.MainNavDrawer;

/**
 * Created by SAMAR on 2/18/2016.
 */


public class MainActivity extends BaseAuthenticatedActivity implements View.OnClickListener, MainActivityAdapter.MainActivityListener {
    private MainActivityAdapter adapter;
    private List<Message> messages;
    private List<ContactRequest> contactRequests;

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inbox");
        setNavDrawer(new MainNavDrawer(this));

        findViewById(R.id.activity_main_newMessageButton).setOnClickListener(this);

        adapter = new MainActivityAdapter(this, this);
        messages = adapter.getMessages();
        contactRequests = adapter.getContactRequests();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_main_recyclerView);
        recyclerView.setAdapter(adapter);


        if (isTablet) {

            GridLayoutManager manager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(manager);
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return 2;
                    }

                    if (contactRequests.size() > 0 && position == contactRequests.size() + 1) {
                        return 2;
                    }

                    return 1;
                }
            });

        } else {

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            scheduler.invokeEveryMillisecond(new Runnable() {
                @Override
                public void run() {
                    onRefresh();
                }
            }, 1000 * 60 * 3); //1000 Millisecond * 60 Seconds * 3 Minute
        }

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.activity_main_newMessageButton) {
            startActivity(new Intent(this, NewMessageActivity.class));
        }

    }

    @Override
    public void onRefresh() {
        swipeRefresh.setRefreshing(true);
        bus.post(new Messages.SearchMessagesRequest(false, true));
        bus.post(new Contacts.GetContactRequest(false));
    }

    @Subscribe
    public void onMessagesLoaded(final Messages.SearchMessagesResponse response) {
        scheduler.invokeOnResume(response.getClass(), new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                if (!response.didSucceed()) {
                    response.showErrorToast(MainActivity.this);
                    return;
                }

                messages.clear();
                messages.addAll(response.Message);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe
    public void onContactRequestLoad(final Contacts.GetContactRequestResponse response) {
        scheduler.invokeOnResume(response.getClass(), new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);

                if (!response.didSucceed()) {
                    response.showErrorToast(MainActivity.this);
                    return;
                }

                contactRequests.clear();
                contactRequests.addAll(response.Request);


            }
        });
    }

    @Override
    public void onMessageClicked(Message message) {

    }

    @Override
    public void onContactRequestClicked(ContactRequest request, int position) {

    }
}
