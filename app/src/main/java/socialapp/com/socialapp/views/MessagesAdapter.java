package socialapp.com.socialapp.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import socialapp.com.socialapp.Activities.BaseActivity;
import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.entities.Message;

/**
 * Created by SAMAR on 4/17/2016.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private final BaseActivity activity;
    private final onMessageClickedListener listener;
    private ArrayList<Message> messages;

    public MessagesAdapter(onMessageClickedListener listener, BaseActivity activity) {
        this.activity = activity;
        this.listener = listener;
        messages = new ArrayList<>();
        layoutInflater = activity.getLayoutInflater();

    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.list_item_message, parent, false);
        v.setOnClickListener(this);
        return new MessageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.populate(activity, messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof Message) {
            Message message = (Message) v.getTag();
            listener.onMessageClicked(message);
        }
    }

    public interface onMessageClickedListener {
        void onMessageClicked(Message message);
    }
}
