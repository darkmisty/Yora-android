package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Messages;
import socialapp.com.socialapp.Services.entities.Message;
import socialapp.com.socialapp.views.MainNavDrawer;
import socialapp.com.socialapp.views.MessagesAdapter;

/**
 * Created by SAMAR on 3/1/2016.
 */
public class SendMessagesActivity extends BaseAuthenticatedActivity implements MessagesAdapter.onMessageClickedListener {

    private static final int REQUEST_VIEW_MESSAGE = 1;

    private MessagesAdapter adapter;
    private ArrayList<Message> messages;
    private View progressFrame;

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_sent_messages);
        setNavDrawer(new MainNavDrawer(this));
        getSupportActionBar().setTitle("Sent Messages");

        adapter = new MessagesAdapter(this, this);
        messages = adapter.getMessages();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_send_messages_messages);
        recyclerView.setAdapter(adapter);

        if (isTablet) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        progressFrame = findViewById(R.id.activity_send_messages_progressFrame);

        scheduler.postEveryMilliseconds(new Messages.SearchMessagesRequest(true, false), 1000 * 60 * 3);


    }


    @Override
    public void onMessageClicked(Message message) {
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra(MessageActivity.EXTRA_MESSAGE, message);
        startActivityForResult(intent, REQUEST_VIEW_MESSAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_VIEW_MESSAGE || resultCode != MessageActivity.REQUEST_IMAGE_DELETED) {
            return;
        }

        int messageId = data.getIntExtra(MessageActivity.RESULT_EXTRA_MESSAGE_ID, -1);
        if (messageId == -1) {
            return;
        }

        for (int i = 0; i < messages.size(); i++) {
            Message message = messages.get(i);

            if (message.getId() != messageId) {
                continue;
            }

            messages.remove(i);
            adapter.notifyItemRemoved(i);
            break;
        }
    }


    @Subscribe
    public void onMessageLoaded(Messages.SearchMessagesResponse response) {
        response.showErrorToast(this);

        int oldMessageSize = messages.size();
        messages.clear();
        adapter.notifyItemRangeRemoved(0, oldMessageSize);

        messages.addAll(response.Message);
        adapter.notifyItemRangeInserted(0, messages.size());

        progressFrame.setVisibility(View.GONE);
    }
}
