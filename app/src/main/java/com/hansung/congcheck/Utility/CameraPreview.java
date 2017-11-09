package com.hansung.congcheck.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.hansung.congcheck.R;

import java.io.IOException;

/**
 * Created by user5 on 2017-10-17.
 */

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private String TAG = "CameraPreview";
    public CameraPreview(Context context, Camera camera){
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    // The Surface has been created, now tell the camera where to draw the preview.
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = null;
        try {
            setWillNotDraw(false);
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }finally {
            if(canvas != null){
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    // If your preview can change or rotate, take care of those events here.
    // Make sure to stop the preview before resizing or reformatting it.
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        Paint p = new Paint(Color.RED);
        Bitmap frame = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        canvas.drawBitmap(frame, 50, 100, p);
    }

    public int getDisplayRotation(){
        Display display = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degree = 0;

        switch (rotation){
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
            default:
                degree = 0;
                break;
        }
        degree = (90 - degree + 360) % 360;
        return degree;
    }
}
