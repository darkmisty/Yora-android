package socialapp.com.socialapp.Activities;


import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.soundcloud.android.crop.Crop;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import socialapp.com.socialapp.Dialogs.ChangePasswordDialog;
import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.Account;
import socialapp.com.socialapp.Utilities.Utils;
import socialapp.com.socialapp.infrastructure.User;
import socialapp.com.socialapp.views.MainNavDrawer;


public class ProfileActivity extends BaseAuthenticatedActivity implements View.OnClickListener {

    private static final int REQUEST_SELECT_IMAGE = 100;
    private static final int STATE_VIEWING = 1;
    private static final int STATE_EDITING = 2;

    private static final String BUNDLE_STATE = "BUNDLE_STATE";
    private static boolean isProgressBarVisible;

    private int currentState;
    private EditText emailText, displayNameText;
    private View changeAvatarButton;
    private ActionMode editProfileActionMode;
    private ImageView avatarView;
    private View avatarProgressFrame;
    private File tempOutputFile;
    private Dialog progressDialog;

    @Override
    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_profile);
        setNavDrawer(new MainNavDrawer(this));

        if (!isTablet) {

            View textFields = findViewById(R.id.activity_profile_textFields);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textFields.getLayoutParams();

            //layout_marginTop = value of currently layout_marginStart which is 18dp
            params.setMargins(0, params.getMarginStart(), 0, 0);

            //Removing this rule "android:layout_toEndOf="@+id/activity_profile_avatar"
            params.removeRule(RelativeLayout.END_OF);


            //Adding another rule "android:layout_below="@+id/activity_profile_changeAvatar"
            params.addRule(RelativeLayout.BELOW, R.id.activity_profile_changeAvatar);


            //finally committing params to textFields
            textFields.setLayoutParams(params);
        }

        displayNameText = (EditText) findViewById(R.id.activity_profile_displayName);
        emailText = (EditText) findViewById(R.id.activity_profile_email);
        avatarView = (ImageView) findViewById(R.id.activity_profile_avatar);
        avatarProgressFrame = findViewById(R.id.activity_profile_avatarProgressFrame);
        changeAvatarButton = findViewById(R.id.activity_profile_changeAvatar);
        tempOutputFile = new File(getExternalCacheDir(), "temp-img.jpg");


        avatarView.setOnClickListener(this);
        changeAvatarButton.setOnClickListener(this);
        avatarProgressFrame.setVisibility(View.GONE);


        if (savedState == null) {
            User user = application.getAuth().getUser();
            getSupportActionBar().setTitle(user.getDisplayName());
            displayNameText.setText(user.getDisplayName());
            emailText.setText(user.getEmail());
            changeState(STATE_VIEWING);
        } else {
            changeState(savedState.getInt(BUNDLE_STATE));
        }

        if (isProgressBarVisible)
            setProgressBarVisible(true);

    }


    @Subscribe
    public void userDetialChangeEvent(Account.UserDetailUpdatesEvent event) {
        getSupportActionBar().setTitle(event.user.getDisplayName());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE, currentState);
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        if (viewId == R.id.activity_profile_changeAvatar || viewId == R.id.activity_profile_avatar) {
            changeAvatar();
        }

    }

    private void changeAvatar() {

        //List of all the intents that are capable of capturing picture from camera will store in it
        List<Intent> otherImageCaptureIntents = new ArrayList<>();

        //fetching the list of all the camera capturing intents in the phone
        List<ResolveInfo> otherImageCaputerActivities = getPackageManager()
                .queryIntentActivities(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);

        //Setting up a list of camera intents and their names and icons
        for (ResolveInfo info : otherImageCaputerActivities) {

            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.setClassName(info.activityInfo.packageName, info.activityInfo.name);

            //where to store image which will be taken from captureIntent
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempOutputFile));
            otherImageCaptureIntents.add(captureIntent);
        }

        //Pick image from gallery intent
        Intent selectImageIntent = new Intent(Intent.ACTION_PICK);
        selectImageIntent.setType("image/*");


        //Create A chooser Dialog for intents
        Intent chooser = Intent.createChooser(selectImageIntent, "Chooser Avatar");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, otherImageCaptureIntents.toArray(new Parcelable[otherImageCaptureIntents.size()]));

        startActivityForResult(chooser, REQUEST_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            tempOutputFile.delete();
            return;
        }

        if (requestCode == REQUEST_SELECT_IMAGE) {
            Uri outputFile;
            Uri tempFileUri = Uri.fromFile(tempOutputFile);

            //If we take it from the gallery
            if (data != null && (data.getAction() == null || data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE))) {
                outputFile = data.getData();
                Utils.tmsg(this, "From Internal Storage");

                //If we take it from camera
            } else {
                outputFile = tempFileUri;
                Utils.tmsg(this, "From Camera");

            }

            //Open sound cloud crop activity
            new Crop(outputFile)
                    .asSquare()
                    .output(tempFileUri)
                    .start(this);

        } else if (requestCode == Crop.REQUEST_CROP) {
            avatarProgressFrame.setVisibility(View.VISIBLE);
            bus.post(new Account.ChangeAvatarRequest(Uri.fromFile(tempOutputFile)));
        }


    }


    @Subscribe
    public void onAvatarUpdated(Account.ChangeAvatarResponse response) {

        avatarProgressFrame.setVisibility(View.GONE);

        if (!response.didSucceed()) {
            response.showErrorToast(this);
        }
    }

    @Subscribe
    public void onProfileUpdate(Account.UpdateProfileResponse response) {

        if (!response.didSucceed()) {
            response.showErrorToast(this);
            changeState(STATE_EDITING);

        }

        displayNameText.setError(response.getPropertyError("displayName"));
        emailText.setError(response.getPropertyError("EmailError"));


        setProgressBarVisible(false);

    }

    private void setProgressBarVisible(boolean visible) {

        if (visible) {
            progressDialog = new ProgressDialog.Builder(this)
                    .setTitle("Updating Profile")
                    .setCancelable(false)
                    .show();

        } else if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

        isProgressBarVisible = visible;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_profile, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();

        if (itemid == R.id.activity_profile_menuEdit) {

            changeState(STATE_EDITING);
            return true;

        } else if (itemid == R.id.activity_profile_menuChangePassword) {

            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null);

            ChangePasswordDialog dialog = new ChangePasswordDialog();
            dialog.show(transaction, null);
            return true;
        }


        return false;
    }


    private void changeState(int state) {
        if (state == currentState)
            return;

        currentState = state;

        if (state == STATE_VIEWING) {

            displayNameText.setEnabled(false);
            emailText.setEnabled(false);
            changeAvatarButton.setVisibility(View.VISIBLE);

            //Finish Contextual Actionbar if its showing and set its value to null
            if (editProfileActionMode != null) {
                editProfileActionMode.finish();
                editProfileActionMode = null;
            }


        } else if (state == STATE_EDITING) {

            displayNameText.setEnabled(true);
            emailText.setEnabled(true);
            changeAvatarButton.setVisibility(View.GONE);

            //start Contextual ActionBad
            editProfileActionMode = toolbar.startActionMode(new EditProfileIsActionCallback());


        } else
            throw new IllegalArgumentException("Invalid state : " + state);

    }

    /*Contextual Actionbar logic Class
    * If we want to make Contextual actionbar to overlay app toolbar we have to
    * add set windowActionModeOverlay = true in style xml
    * */
    private class EditProfileIsActionCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            getMenuInflater().inflate(R.menu.activity_profile_edit, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            int itemId = item.getItemId();

            if (itemId == R.id.activity_profile_menuDone) {
                setProgressBarVisible(true);
                changeState(STATE_VIEWING);

                bus.post(new Account.UpdateProfileRequest(
                        displayNameText.getText().toString()
                        , emailText.getText().toString()
                ));

                return true;


            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (currentState != STATE_VIEWING)
                changeState(STATE_VIEWING);
        }
    }
}

























