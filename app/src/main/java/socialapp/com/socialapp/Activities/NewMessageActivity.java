package socialapp.com.socialapp.Activities;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.FrameLayout;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Utilities.Utils;
import socialapp.com.socialapp.views.CameraPreview;

/**
 * Created by SAMAR on 4/18/2016.
 */
public class NewMessageActivity extends BaseAuthenticatedActivity {

    private static final String TAG = "NewMessageActivity";
    private static final String STATE_CAMERA_INDEX = "STATE_CAMERA_INDEX";

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    private Camera camera;
    private Camera.CameraInfo cameraInfo;
    private int currentCameraIndex;
    private CameraPreview cameraPreview;

    protected void onSocialCreate(Bundle savedState) {
        setContentView(R.layout.activity_new_message);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (savedState != null) {
            currentCameraIndex = savedState.getInt(STATE_CAMERA_INDEX);
        } else {
            currentCameraIndex = 0;
        }

        cameraPreview = new CameraPreview(this);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.activity_new_message_frame);
        frameLayout.addView(cameraPreview, 0);



    }

    @Override
    protected void onResume() {
        super.onResume();
        establishCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            cameraPreview.setCamera(null, null);
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(STATE_CAMERA_INDEX, currentCameraIndex);
    }

    private void establishCamera() {
        if (camera != null) {
            cameraPreview.setCamera(null, null);
            camera.release();
            camera = null;
        }

        try {
            camera = Camera.open(currentCameraIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not open camrea" + currentCameraIndex, e);
            Utils.tmsg(this, "Error establising camrea!");
            return;
        }

        cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(currentCameraIndex, cameraInfo);

        cameraPreview.setCamera(camera, cameraInfo);

        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
            Utils.tmsg(this, "Using Back Camera");
        } else {
            Utils.tmsg(this, "Using Front Camera");
        }
    }
}
