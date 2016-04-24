package socialapp.com.socialapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import socialapp.com.socialapp.Activities.BaseActivity;
import socialapp.com.socialapp.Services.entities.ContactRequest;
import socialapp.com.socialapp.Services.entities.Message;

/**
 * Created by SAMAR on 4/24/2016.
 */
public class MainActivityAdapter extends RecyclerView.Adapter {

    private List<Message> messages;
    private List<ContactRequest> contactRequests;
    private BaseActivity activity;
    private LayoutInflater inflater;
    private MainActivityListener listener;

    public MainActivityAdapter(BaseActivity activity, MainActivityListener listener) {
        this.activity = activity;
        this.listener = listener;
        inflater = activity.getLayoutInflater();
        messages = new ArrayList<>();
        contactRequests = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<ContactRequest> getContactRequests() {
        return contactRequests;
    }

    public interface MainActivityListener {

        void onMessageClicked(Message message);

        void onContactRequestClicked(ContactRequest request, int position);
    }
}
