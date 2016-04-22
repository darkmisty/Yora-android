package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Messages;
import socialapp.com.socialapp.Services.entities.UserDetails;

/**
 * Created by SAMAR on 4/22/2016.
 */
public class SendMessageActivity extends BaseAuthenticatedActivity implements View.OnClickListener {


    public static final String EXTRA_IMAGE_PATH = "EXTRA_IMAGE_PATH";
    public static final String  EXTRA_CONTACT = "EXTRA_CONTACT";

    public static final int MAX_IMAGE_HEIGHT = 1280;
    private static final String STATE_REQUEST = "STATE_REQUEST";
    private static final int REQUEST_SELECT_RECIPIENT = 1;

    private Messages.SendMessageRequest request;
    private EditText messageEditText;
    private Button recipientButton;
    private View progressFrame;

    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_send_message);
        getSupportActionBar().setTitle("Send Message");

        if (savedState != null) {
            request = savedState.getParcelable(STATE_REQUEST);
            request.setRecipient((UserDetails) getIntent().getParcelableExtra(EXTRA_CONTACT));

        }

        if (request == null) {
            request = new Messages.SendMessageRequest();
        }

        Uri imageUri = getIntent().getParcelableExtra(EXTRA_IMAGE_PATH);
        if (imageUri != null) {
            ImageView image = (ImageView) findViewById(R.id.activity_send_message_image);
            Picasso.with(this).load(imageUri).into(image);
        }

        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
            View optionFrame = findViewById(R.id.activity_sendmessage_optionsFrame);

            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) optionFrame.getLayoutParams();

            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.BELOW, R.id.include_toolbar);
            params.width =
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());

            optionFrame.setLayoutParams(params);
        }

        progressFrame = findViewById(R.id.activity_send_message_progressFrame);
        recipientButton = (Button) findViewById(R.id.activity_send_message_recipient);
        messageEditText = (EditText) findViewById(R.id.activity_send_message_message);


        progressFrame.setVisibility(View.GONE);

        recipientButton.setOnClickListener(this);
        updateButtons();




    }


    @Override
    public void onClick(View v) {

        if (v == recipientButton) {
            selectRecipient();
        }
    }

    private void updateButtons() {
        UserDetails recipient = request.getRecipient();

        if (recipient != null) {
            recipientButton.setText("To: " + recipient.getDisplayName());
        } else {
            recipientButton.setText("Choose Recipient");
        }

    }


    private void selectRecipient() {
        startActivityForResult(new Intent(this, SelectedContactActivity.class), REQUEST_SELECT_RECIPIENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_RECIPIENT && resultCode == RESULT_OK) {
            UserDetails selectedContact = data.getParcelableExtra(SelectedContactActivity.RESULT_CONTACT);
            request.setRecipient(selectedContact);
            updateButtons();
        }
    }
}
