package socialapp.com.socialapp.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import socialapp.com.socialapp.Activities.BaseActivity;
import socialapp.com.socialapp.Activities.ContactsActivity;
import socialapp.com.socialapp.Activities.MainActivity;
import socialapp.com.socialapp.Activities.ProfileActivity;
import socialapp.com.socialapp.Activities.SendMessagesActivity;
import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Account;
import socialapp.com.socialapp.infrastructure.User;


public class MainNavDrawer extends NavDrawer {

    private final TextView displayNameText;
    private final ImageView avatarImage;


    public MainNavDrawer(final BaseActivity activity) {
        super(activity);

        addItem(new ActivityNavDrawerItem(MainActivity.class, "Inbox", null, R.drawable.ic_action_unread, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(SendMessagesActivity.class, "Sent Messages", null, R.drawable.ic_action_send_now, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(ContactsActivity.class, "Contacts", null, R.drawable.ic_action_group, R.id.include_main_nav_drawer_topItems));
        addItem(new ActivityNavDrawerItem(ProfileActivity.class, "Profile", null, R.drawable.ic_action_group, R.id.include_main_nav_drawer_topItems));

        addItem(new BasicNavDrawerItem("Logout", null, R.drawable.ic_action_backspace, R.id.include_main_nav_drawer_bottomItems) {
            @Override
            public void onClick(View v) {
                super.onClick(v);
                activity.getMyApplication().getAuth().logout();

            }
        });


        displayNameText = (TextView) navDrawerView.findViewById(R.id.include_main_nav_drawer_displayName);
        avatarImage = (ImageView) navDrawerView.findViewById(R.id.include_main_nav_drawer_avatar);

        User loggedInUser = activity.getMyApplication().getAuth().getUser();
        displayNameText.setText(loggedInUser.getDisplayName());

        //Todo : change avatar image to avatar uri from loggedInUser

    }

    @Subscribe
    public void userDetialChangeEvent(Account.UserDetailUpdatesEvent event) {


        //Todo: Update Avatar Url
        displayNameText.setText(event.user.getDisplayName());
    }


}
