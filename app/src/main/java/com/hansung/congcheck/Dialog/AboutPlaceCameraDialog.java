package com.hansung.congcheck.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hansung.congcheck.Activity.AboutPlaceActivity;
import com.hansung.congcheck.Activity.CameraActivity;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.Constants;
import com.hansung.congcheck.Utility.SharedPrefManager;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by user5 on 2017-11-10.
 */

public class AboutPlaceCameraDialog extends DialogFragment {

    public LinearLayout selectImage;
    public LinearLayout selectCamera;
    private int PlaceNumber = Constants.PLACENUMBER.NAKSANROAD; //Default Value;
    int INTENT_RESULT_GALLERY_CODE = 1001;

    LinearLayout.OnClickListener ll_onClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.dialog_aboutplace_camera, container, false);
        selectImage = (LinearLayout)view.findViewById(R.id.ll_dialog_selectimage);
        selectCamera = (LinearLayout)view.findViewById(R.id.ll_dialog_camera);


        setll_onClickListener();

        selectImage.setOnClickListener(ll_onClickListener);
        selectCamera.setOnClickListener(ll_onClickListener);

        return view;
    }

    public int getPlaceNumber() {
        return PlaceNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        PlaceNumber = placeNumber;
    }

    public void setll_onClickListener(){
        ll_onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.ll_dialog_selectimage:
                        Intent intent1 = new Intent(Intent.ACTION_PICK);
                        intent1.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent1.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent1, INTENT_RESULT_GALLERY_CODE);
                        break;
                    case R.id.ll_dialog_camera:
                        Snackbar.make(view, "카메라를 실행합니다.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        Intent intent = new Intent(getContext(), CameraActivity.class);
                        intent.putExtra("PLACENUMBER", getPlaceNumber());
                        getContext().startActivity(intent);
                        break;
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == INTENT_RESULT_GALLERY_CODE){
                getPictureURLFromGallery(data);
            }
        }
    }

    private void getPictureURLFromGallery(Intent intentData){
        Uri imgUri = intentData.getData();
        String convertPath = getRealPathFromURI(imgUri);
        SharedPrefManager pref = SharedPrefManager.getInstance(this.getContext());
        switch (getPlaceNumber()){
            case Constants.PLACENUMBER.NAKSANROAD:
                pref.setPrefDataNaksanroadimage(convertPath);
                break;
            case Constants.PLACENUMBER.NAKSANPARK:
                pref.setPrefDataNaksanparkimage(convertPath);
                break;
            case Constants.PLACENUMBER.HYEHWADOOR:
                pref.setPrefDataHyehwadoorimage(convertPath);
                break;
            case Constants.PLACENUMBER.HANSUNGUNI:
                pref.setPrefDataHansunguniimage(convertPath);
                break;
        }
        Toast.makeText(getContext(),"사진이 등록 되었습니다.", Toast.LENGTH_SHORT).show();

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(
                this.getActivity(),
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onResume(){
        super.onResume();
        Window window = getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
