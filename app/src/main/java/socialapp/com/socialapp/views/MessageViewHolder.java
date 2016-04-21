package socialapp.com.socialapp.views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.entities.Message;

/**
 * Created by SAMAR on 4/17/2016.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    private ImageView avatar;
    private TextView displayName, createdAt, sentReceived;
    private CardView cardView;

    public MessageViewHolder(View v) {
        super(v);
        cardView = (CardView) v;
        avatar = (ImageView) v.findViewById(R.id.list_item_message_avatar);
        displayName = (TextView) v.findViewById(R.id.list_item_message_displayName);
        createdAt = (TextView) v.findViewById(R.id.list_item_message_createdAt);
        sentReceived = (TextView) v.findViewById(R.id.list_item_message_sendReceived);
    }

    public void populate(Context context, Message message) {
        itemView.setTag(message);

        Picasso.with(context).load(message.getOtherUser().getAvatarUrl()).into(avatar);

        String createdAt = DateUtils.formatDateTime(
                context,
                message.getCreatedAt().getTimeInMillis()
                , DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);


        sentReceived.setText(message.isFromUs() ? "send" : "received");
        displayName.setText(message.getOtherUser().getDisplayName());
        this.createdAt.setText(createdAt);

        int colorResourceId;

        if (message.isSelected()) {
            colorResourceId = R.color.list_item_message_background_selected;
            cardView.setCardElevation(5);
        } else if (message.isRead()) {
            colorResourceId = R.color.list_item_message_background;
            cardView.setCardElevation(2);
        } else {
            colorResourceId = R.color.list_item_message_background_unread;
            cardView.setCardElevation(3);
        }

        cardView.setCardBackgroundColor(context.getResources().getColor(colorResourceId));
    }
}
