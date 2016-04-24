package socialapp.com.socialapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import socialapp.com.socialapp.R;
import socialapp.com.socialapp.Services.entities.Message;
import socialapp.com.socialapp.Services.entities.UserDetails;
import socialapp.com.socialapp.Utilities.Utils;
import socialapp.com.socialapp.views.CameraPreview;


public class NewMessageActivity extends BaseAuthenticatedActivity implements View.OnClickListener, Camera.PictureCallback {

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private static final String TAG = "NewMessageActivity";
    private static final String STATE_CAMERA_INDEX = "STATE_CAMERA_INDEX";
    private static final int REQUEST_SEND_MESSAGE = 1;

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


        findViewById(R.id.activity_new_message_switchCamera).setOnClickListener(this);
        findViewById(R.id.activity_new_message_takePicture).setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_new_message_takePicture:
                takePicture();
                break;
            case R.id.activity_new_message_switchCamera:
                switchCamera();
                break;
        }

    }

    private void switchCamera() {

        currentCameraIndex = currentCameraIndex + 1 < Camera.getNumberOfCameras() ? currentCameraIndex + 1 : 0;
        establishCamera();



    }

    private void takePicture() {
        camera.takePicture(null, null, this);
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
            Utils.tmsg(this, "Error establishing camera!");
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

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = processBitmap(data);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, output);

        File outputFile = new File(getCacheDir(), "temp-image");
        outputFile.delete();
        try {
            FileOutputStream fileOutput = new FileOutputStream(outputFile);
            fileOutput.write(output.toByteArray());
            fileOutput.close();
        } catch (Exception e) {
            Log.e(TAG, "Could not save bitmap");
            Utils.tmsg(this, "Could not save image to temp directory");
            return;
        }

        Intent i = new Intent(this, SendMessageActivity.class);
        i.putExtra(SendMessageActivity.EXTRA_IMAGE_PATH, Uri.fromFile(outputFile));

        UserDetails user = getIntent().getParcelableExtra(EXTRA_CONTACT);

        if (user != null) {
            i.putExtra(SendMessageActivity.EXTRA_CONTACT, user);
        }

        startActivityForResult(i, REQUEST_SEND_MESSAGE);
        bitmap.recycle();

    }

    @SuppressWarnings("SuspiciousNameCombination")
    private Bitmap processBitmap(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (bitmap.getWidth() > SendMessageActivity.MAX_IMAGE_HEIGHT) {
            float scale = (float) SendMessageActivity.MAX_IMAGE_HEIGHT / bitmap.getWidth();
            int finalWidth = (int) (bitmap.getHeight() * scale);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, SendMessageActivity.MAX_IMAGE_HEIGHT, finalWidth, false);

            if (resizedBitmap != bitmap) {
                bitmap.recycle();
                bitmap = resizedBitmap;
            }

        }

        Matrix matrix = new Matrix();
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            Matrix matrixMirror = new Matrix();
            matrixMirror.setValues(new float[] {
                    -1, 0, 0,
                    0, 1, 0,
                    0, 0, 1
            });

            matrix.postConcat(matrixMirror);
        }

        matrix.postRotate(90);
        Bitmap processedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (bitmap != processedBitmap) {
            bitmap.recycle();
        }

        return processedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_SEND_MESSAGE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();

            Message message = data.getParcelableExtra(SendMessageActivity.RESULT_MESSAGE);

            Intent intent = new Intent();
            intent.putExtra(MessageActivity.EXTRA_MESSAGE, message);
            startActivity(intent);

        }
    }
}
