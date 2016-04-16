package socialapp.com.socialapp.views;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import socialapp.com.socialapp.Activities.BaseActivity;
import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.entities.ContactRequest;

/**
 * Created by SAMAR on 4/13/2016.
 */
public class ContactRequestAdapter extends ArrayAdapter<ContactRequest> {

    private LayoutInflater inflater;

    public ContactRequestAdapter(BaseActivity activity) {
        super(activity, 0);
        inflater = activity.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactRequest request = getItem(position);

        ViewHolder view;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_contact_request, parent, false);
            view = new ViewHolder(convertView);
            convertView.setTag(view);

        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.DisplayName.setText(request.getUser().getDisplayName());
        Picasso.with(getContext()).load(request.getUser().getAvatarUrl()).into(view.Avatar);

        String CreatedAt = DateUtils.formatDateTime(
                getContext(),
                request.getCreatedAt().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
        );

        if (request.isFromUs()) {
            view.CreatedAt.setText("Sent at " + CreatedAt);
        } else {
            view.CreatedAt.setText("Received " + CreatedAt);
        }

        return convertView;

    }

    private class ViewHolder {
        public TextView DisplayName;
        public TextView CreatedAt;
        public ImageView Avatar;

        public ViewHolder(View v) {

            DisplayName = (TextView) v.findViewById(R.id.list_item_contact_request_displayName);
            CreatedAt = (TextView) v.findViewById(R.id.list_item_contact_request_createdAt);
            Avatar = (ImageView) v.findViewById(R.id.list_item_contact_request_avatar);

        }
    }


}
