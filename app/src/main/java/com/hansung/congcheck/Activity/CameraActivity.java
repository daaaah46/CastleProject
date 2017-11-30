package com.hansung.congcheck.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.hansung.congcheck.Dialog.CameraSelectStickerDialog;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.CameraPreview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CameraActivity extends AppCompatActivity {

    private static String TAG = "CameraPreview";
    private Camera mCamera;
    private CameraPreview mPreview;
    private static String AbsolutePath;
    private boolean takePicture = false;

    /**
     * 상수 정의
     */
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int FLASH_MODE_ON = 2;
    public static final int FLASH_MODE_OFF = 3;
    public static final int FLASH_MODE_TOUCH = 4;
    public static final int RATIO_MODE_4_3 = 5;
    public static final int RATIO_MODE_1_1 = 6;
    public static final int TIMER_MODE_0 = 7;
    public static final int TIMER_MODE_3 = 8;
    public static final int TIMER_MODE_5 = 9;
    public static final int TIMER_MODE_10 = 10;
    public static final int Sticker_MODE_ON = 11;
    public static final int Sticker_MODE_OFF = 12;

    public final int REQUEST_CAMERA = 1123;

    private int FlashMode;
    private int RatioMode;
    private int TimerMode;
    private int StickerMode;

    /**
     * activity_camera UI 연결
     */
    ImageButton flash;
    ImageButton ratio;
    ImageButton timer;
    ImageButton sticker;
    ImageButton captureButton;
    ImageButton.OnClickListener onClickListener;
    ImageView stickerImage;

    FrameLayout preview;
    Timer timerref;
    TimerTask timerTask;

    Bitmap overlayImage;
    boolean getBitmap = false;

    CameraSelectStickerDialog stickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //permission check
            int camerapermissionCheck = checkSelfPermission(Manifest.permission.CAMERA);
            int storagepermissionCheck = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //권한이 부여되지 않았을 경우 : -1(PERMISSION_DENIED). 부여된 경우 0(PERMISSION_GRANTED)
            if (camerapermissionCheck == PackageManager.PERMISSION_DENIED || storagepermissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            }
            else if (camerapermissionCheck == PackageManager.PERMISSION_GRANTED && storagepermissionCheck == PackageManager.PERMISSION_GRANTED) {

                RatioMode = RATIO_MODE_4_3;
                mCamera = getCameraInstance();
                mPreview = new CameraPreview(this, mCamera);
                preview = (FrameLayout) findViewById(R.id.camera_preview);
                setPreviewSize();
            }
        }

        FlashMode = FLASH_MODE_OFF;
        TimerMode = TIMER_MODE_0;
        StickerMode = Sticker_MODE_OFF;



        // Add a listener to the Capture button
        captureButton   = (ImageButton) findViewById(R.id.btn_camera_capture);
        flash            = (ImageButton) findViewById(R.id.btn_camera_flash);
        ratio            = (ImageButton) findViewById(R.id.btn_camera_ratio);
        sticker          = (ImageButton) findViewById(R.id.btn_camera_sticker);
        timer            = (ImageButton) findViewById(R.id.btn_camera_timer);
        stickerImage    = (ImageView)   findViewById(R.id.iv_camera_sticker);

        ibtnOnClickListener();

        captureButton.setOnClickListener(onClickListener);
        flash.setOnClickListener(onClickListener);
        ratio.setOnClickListener(onClickListener);
        sticker.setOnClickListener(onClickListener);
        timer.setOnClickListener(onClickListener);

    }

    private void ibtnOnClickListener(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btn_camera_capture:
                        if(TimerMode == TIMER_MODE_0){
                            mCamera.takePicture(mSutter,null,mPicture);
                            takePicture = true;
                        }else if(TimerMode == TIMER_MODE_3){
                            setTimerReference(3000);
                        }else if(TimerMode == TIMER_MODE_5){
                            setTimerReference(5000);
                        }else if(TimerMode == TIMER_MODE_10){
                            setTimerReference(10000);
                        }
                        break;
                    case R.id.btn_camera_flash:
                        getFlashMode();
                        break;
                    case R.id.btn_camera_ratio:
                        getRatioMode();
                        break;
                    case R.id.btn_camera_sticker:
                        setSticker();
                        break;
                    case R.id.btn_camera_timer:
                        setTimerMode();
                        break;
                }
            }
        };
    }

    private void setTimerReference(int millsecond){
        timerref = new Timer();
        timerTask = new TimerTask(){
            @Override
            public void run(){
                mCamera.takePicture(mSutter,null,mPicture);
                takePicture = true;
            }
        };
        timerref.schedule(timerTask, millsecond);
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
            int w = cameraBitmap.getWidth();
            int h = cameraBitmap.getHeight();

            Bitmap newImage = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newImage);
            canvas.drawBitmap(cameraBitmap, 0f, 0f, null);

            /*Drawable d = getResources().getDrawable(R.mipmap.theother_sticker_big_naksanroad);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            d.draw(canvas);*/

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
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Congcheck");

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

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private void getoverlayBitmap(){
        getBitmap = true;
        overlayImage = BitmapFactory.decodeResource(getResources(), R.mipmap.home_site_hansunguni);
        stickerImage.setImageBitmap(overlayImage);
        ((ViewGroup)stickerImage.getParent()).removeView(stickerImage);
        preview.addView(stickerImage);
    }

    public void setSticker(){
        switch (StickerMode){
            case Sticker_MODE_OFF:
                StickerMode = Sticker_MODE_ON;
                if(!getBitmap){ // 비트맵을 한 번만 가져올 수 있게!!
                    getoverlayBitmap();
                }else {
                    stickerImage.setVisibility(View.VISIBLE);
                }
                break;
            case Sticker_MODE_ON:
                StickerMode = Sticker_MODE_OFF;
                stickerImage.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void getFlashMode(){
        switch (FlashMode){
            case FLASH_MODE_OFF:
                FlashMode = FLASH_MODE_TOUCH;
                break;
            case FLASH_MODE_TOUCH:
                FlashMode = FLASH_MODE_ON;
                break;
            case FLASH_MODE_ON:
                FlashMode = FLASH_MODE_OFF;
                break;
        }
        FlashSetting();
    }

    private void setTimerMode(){
        switch (TimerMode){
            case TIMER_MODE_0:
                TimerMode = TIMER_MODE_3;
                timer.setImageResource(R.mipmap.camera_timer_three);
                break;
            case TIMER_MODE_3:
                TimerMode = TIMER_MODE_5;
                timer.setImageResource(R.mipmap.camera_timer_five);
                break;
            case TIMER_MODE_5:
                TimerMode = TIMER_MODE_10;
                timer.setImageResource(R.mipmap.camera_timer_ten);
                break;
            case TIMER_MODE_10:
                TimerMode = TIMER_MODE_0;
                timer.setImageResource(R.mipmap.camera_timer);
                break;
        }
    }

    public void getRatioMode(){
        switch (RatioMode){
            case RATIO_MODE_4_3:
                RatioMode = RATIO_MODE_1_1;
                ratio.setImageResource(R.mipmap.camera_screen_ratio_oneone);
                break;
            case RATIO_MODE_1_1:
                RatioMode = RATIO_MODE_4_3;
                ratio.setImageResource(R.mipmap.camera_screen_ratio_fourthree);
                break;
        }
        //setPreviewSize();
    }

    private void FlashSetting(){
        Camera.Parameters parameters = mCamera.getParameters();
        switch (FlashMode){
            case FLASH_MODE_OFF:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flash.setImageResource(R.mipmap.camera_nonfalsh);
                break;

            case FLASH_MODE_TOUCH:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                flash.setImageResource(R.mipmap.camera_falsh);
                break;

            case FLASH_MODE_ON:
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                flash.setImageResource(R.mipmap.camera_autoflash);
                break;
        }
        mCamera.setParameters(parameters);
    }

    private void setPreviewSize(){
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int previewWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        int previewHeight = 0;
        switch (RatioMode) {
            case RATIO_MODE_4_3:
                previewHeight = previewWidth * 4 / 3;
                break;
            case RATIO_MODE_1_1:
                previewHeight = previewWidth;
                break;
        }
        params.width = previewWidth;
        params.height = previewHeight;
        params.gravity = Gravity.CENTER;
        //preview.removeAllViews();
        preview.addView(mPreview,params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    RatioMode = RATIO_MODE_4_3;
                    mCamera = getCameraInstance();
                    // Create our Preview view and set it as the content of our activity.
                    mPreview = new CameraPreview(this, mCamera);
                    preview = (FrameLayout) findViewById(R.id.camera_preview);
                    //preview.addView(mPreview);
                    setPreviewSize();
                } else {
                    Toast.makeText(getApplicationContext(),"카메라 사용과 저장소를 동의하지 않으면 사용할 수 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }
}