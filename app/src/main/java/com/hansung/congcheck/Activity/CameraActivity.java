package com.hansung.congcheck.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;

import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.CameraPreview;
import com.hansung.congcheck.Utility.Constants;
import com.hansung.congcheck.Utility.SharedPrefManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CameraActivity extends AppCompatActivity {

    private static String TAG = "CameraPreview";
    private Camera mCamera;
    private CameraPreview mPreview;
    private static String AbsolutePath;
    private boolean takePicture = false;
    private int timerNum = 0;


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
    private int FacingMode;

    /**
     * activity_camera UI 연결
     */
    ImageButton flash;
    ImageButton ratio;
    ImageButton timer;
    ImageButton sticker;
    ImageButton captureButton;
    ImageButton transition;
    ImageButton.OnClickListener onClickListener;
    ImageView stickerImage;
    TextView timerText;
    Handler handler;

    FrameLayout preview;
    Timer timerref;
    TimerTask timerTask;

    Bitmap overlayImage;
    boolean getBitmap = false;

    int PLACENUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        FacingMode = Camera.CameraInfo.CAMERA_FACING_BACK;

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

        Intent intent = getIntent();
        PLACENUMBER = intent.getIntExtra("PLACENUMBER",0);


        // Add a listener to the Capture button
        captureButton   = (ImageButton) findViewById(R.id.btn_camera_capture);
        flash            = (ImageButton) findViewById(R.id.btn_camera_flash);
        ratio            = (ImageButton) findViewById(R.id.btn_camera_ratio);
        sticker          = (ImageButton) findViewById(R.id.btn_camera_sticker);
        timer            = (ImageButton) findViewById(R.id.btn_camera_timer);
        transition      = (ImageButton) findViewById(R.id.btn_camera_transition);
        stickerImage    = (ImageView)   findViewById(R.id.iv_camera_sticker);
        timerText       = (TextView)    findViewById(R.id.tv_camera_timer);


        ibtnOnClickListener();

        captureButton.setOnClickListener(onClickListener);
        flash.setOnClickListener(onClickListener);
        ratio.setOnClickListener(onClickListener);
        sticker.setOnClickListener(onClickListener);
        timer.setOnClickListener(onClickListener);
        transition.setOnClickListener(onClickListener);

        handler = new Handler();
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
                    case R.id.btn_camera_transition:
                        if(FacingMode == Camera.CameraInfo.CAMERA_FACING_BACK){
                            FacingMode = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        }else {
                            FacingMode = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        releaseCamera();
                        mCamera = getCameraInstance();
                        mPreview = new CameraPreview(CameraActivity.this, mCamera);
                        RatioMode = RATIO_MODE_4_3;
                        ratio.setImageResource(R.mipmap.camera_screen_ratio_fourthree);
                        setPreviewSize();
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
        timerCount(millsecond);
        timerref.schedule(timerTask, millsecond);
    }

    private void timerCount(int millsecond){
        final CountDownTimer timer = new CountDownTimer(millsecond + 1000, 1000) {
            @Override
            public void onTick(long l) {
                if(timerNum > 0) {
                    timerText.setText(String.valueOf(timerNum));
                    timerNum--;
                }
            }

            @Override
            public void onFinish() {
                timerText.setVisibility(View.INVISIBLE);
            }
        };
        timer.start();
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

    public Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(FacingMode); // attempt to get a Camera instance
        }
        catch (Exception e){

        }
        return c;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        if(overlayImage != null) {
            overlayImage.recycle();
        }
        releaseCamera();
        mPreview = null;
        super.onDestroy();
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    private void galleryUpdate(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(AbsolutePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(getApplicationContext(),"사진이 저장되었습니다.", Toast.LENGTH_SHORT).show();
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

            Bitmap getBitmap = rotateImage(BitmapFactory.decodeByteArray(data, 0, data.length));
            if(StickerMode == Sticker_MODE_ON){
                getBitmap = combineImage(getBitmap, overlayImage);
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                getBitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                fos.write(data);
                fos.close();
                if(takePicture){
                    galleryUpdate();
                    setPrefImage();
                }
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

    private Bitmap rotateImage(Bitmap bitmap){
        int degree = 0;
        if(FacingMode == Camera.CameraInfo.CAMERA_FACING_BACK){
            degree = 90;
        }else{
            degree = -90;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private Bitmap combineImage(Bitmap image, Bitmap overlay){
        Bitmap cbImage = null;
        Bitmap scaledoverlay = null;

        scaledoverlay = Bitmap.createScaledBitmap(overlay, image.getWidth(), image.getHeight(), true);
        cbImage = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cbImage);
        canvas.drawBitmap(image, 0f, 0f, null);
        canvas.drawBitmap(scaledoverlay, 0f, 0f, null);
        return cbImage;
    }

    private int getResourceIDfromString(String resName,Context context){
        Resources resources = context.getResources();
        int resID = resources.getIdentifier(resName, "mipmap", context.getPackageName());
        return resID;
    }

    private String setStickerfromPLACENUMBER(int placeNum){
        String stickerURL = new String();
        switch (placeNum){
            case Constants.PLACENUMBER.HYEHWADOOR:
                stickerURL += "sticker_d_";
                break;
            case Constants.PLACENUMBER.HANSUNGUNI:
                stickerURL += "sticker_e_";
                break;

            case Constants.PLACENUMBER.NAKSANROAD:
            case Constants.PLACENUMBER.NAKSANPARK:
                Random random = new Random();
                int stickerNum = random.nextInt(3);
                switch (stickerNum){
                    case 0:
                        stickerURL += "sticker_a_";
                        break;
                    case 1:
                        stickerURL += "sticker_b_";
                        break;
                    case 2:
                        stickerURL += "sticker_c_";
                        break;
                }
                break;
        }
        return stickerURL;
    }

    private void getoverlayBitmap(){
        getBitmap = true;
        String url = setStickerfromPLACENUMBER(PLACENUMBER);
        switch (RatioMode){
            case RATIO_MODE_4_3:
                url += "fournthree";
                overlayImage = BitmapFactory.decodeResource(getResources(), getResourceIDfromString(url, getApplicationContext()));
                Log.e(TAG, R.mipmap.sticker_c_fournthree+"");
                break;
            case RATIO_MODE_1_1:
                url += "onenone";
                overlayImage = BitmapFactory.decodeResource(getResources(), getResourceIDfromString(url, getApplicationContext()));
                break;
        }
        stickerImage.setImageBitmap(overlayImage);
        Log.e(TAG, ((ViewGroup)stickerImage.getParent())+"");
        if((ViewGroup)stickerImage.getParent() != null){
            ((ViewGroup) stickerImage.getParent()).removeView(stickerImage);
        }
        preview.addView(stickerImage);
    }

    public void setSticker(){
        switch (StickerMode){
            case Sticker_MODE_OFF:
                StickerMode = Sticker_MODE_ON;
                if(getBitmap == false) {
                    getoverlayBitmap();
                    stickerImage.setVisibility(View.VISIBLE);
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
                timerNum = 3;
                timerText.setVisibility(View.VISIBLE);
                break;
            case TIMER_MODE_3:
                TimerMode = TIMER_MODE_5;
                timer.setImageResource(R.mipmap.camera_timer_five);
                timerNum = 5;
                timerText.setVisibility(View.VISIBLE);
                break;
            case TIMER_MODE_5:
                TimerMode = TIMER_MODE_10;
                timer.setImageResource(R.mipmap.camera_timer_ten);
                timerNum = 10;
                timerText.setVisibility(View.VISIBLE);
                break;
            case TIMER_MODE_10:
                TimerMode = TIMER_MODE_0;
                timer.setImageResource(R.mipmap.camera_timer);
                timerNum = 0;
                timerText.setVisibility(View.INVISIBLE);
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
        setPreviewSize();
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
        getBitmap = false;
        params.width = previewWidth;
        params.height = previewHeight;
        params.gravity = Gravity.CENTER;
        preview.removeAllViews();
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

    private void setPrefImage(){
        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        switch (PLACENUMBER){
            case Constants.PLACENUMBER.NAKSANROAD:
                pref.setPrefDataNaksanroadimage(AbsolutePath);
                break;
            case Constants.PLACENUMBER.NAKSANPARK:
                pref.setPrefDataNaksanparkimage(AbsolutePath);
                break;
            case Constants.PLACENUMBER.HYEHWADOOR:
                pref.setPrefDataHyehwadoorimage(AbsolutePath);
                break;
            case Constants.PLACENUMBER.HANSUNGUNI:
                pref.setPrefDataHansunguniimage(AbsolutePath);
                break;
        }
    }
}