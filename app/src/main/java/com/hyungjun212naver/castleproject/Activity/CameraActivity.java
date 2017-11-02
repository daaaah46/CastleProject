package com.hyungjun212naver.castleproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {

    private static String TAG = "CameraAct";
    private Camera mCamera;
    private CameraPreview mPreview;
    private static String AbsolutePath;
    private boolean takePicture = false;


    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int FLASH_MODE_ON = 2;
    public static final int FLASH_MODE_OFF = 3;
    private int FlashMode;

    Button flash;
    Button captureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        FlashMode = FLASH_MODE_OFF;

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

        // Add a listener to the Capture button
        captureButton = (Button) findViewById(R.id.btn_camera_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(mSutter,null,mPicture);
                takePicture = true;
            }
        });

        flash = (Button) findViewById(R.id.btn_camera_flash);
        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFlashMode();
            }
        });

    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(takePicture){
            galleryUpdate();
        }
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    private void galleryUpdate(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(AbsolutePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Camera.ShutterCallback mSutter = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            Bitmap cameraBitmap = rotateImage(BitmapFactory.decodeByteArray(data, 0, data.length), 90);
            int w = cameraBitmap.getWidth();int h = cameraBitmap.getHeight();

            Bitmap newImage = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newImage);
            canvas.drawBitmap(cameraBitmap, 0f, 0f, null);

            Drawable d = getResources().getDrawable(R.mipmap.ic_launcher_round);
            d.setBounds(50, 100, d.getIntrinsicWidth()+50, d.getIntrinsicHeight()+100);
            d.draw(canvas);

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                newImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        /**
         * SD카드가 마운트 되어있는지 확인하는 코드 필요함
         // To be safe, you should check that the SDCard is mounted
         // using Environment.getExternalStorageState() before doing this.
         */

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Congcheck");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.


        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            AbsolutePath = mediaFile.getAbsolutePath();
        } else {
            return null;
        }
        return mediaFile;
    }

    private Bitmap rotateImage(Bitmap bitmap, int degree){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0,0,w,h,mtx, true);
    }

    public void getFlashMode(){
        switch (FlashMode){
            case FLASH_MODE_ON:
                FlashMode = FLASH_MODE_OFF;
                break;
            case FLASH_MODE_OFF:
                FlashMode = FLASH_MODE_ON;
                break;
        }
        FlashSetting();
    }

    private void FlashSetting(){
        Camera.Parameters parameters = mCamera.getParameters();
        switch (FlashMode){
            case FLASH_MODE_ON:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                break;
            case FLASH_MODE_OFF:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                break;
        }
        mCamera.setParameters(parameters);
    }
}