package com.hansung.congcheck.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hansung.congcheck.Dialog.AboutPlaceCameraDialog;
import com.hansung.congcheck.Dialog.AboutPlaceImageDialog;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.Constants;
import com.hansung.congcheck.Utility.SharedPrefManager;

import java.io.File;
import java.util.ArrayList;

public class AboutPlaceActivity extends AppCompatActivity {

    String TAG = "AboutPlaceActivity";
    int imgviewid = 5;

    int PLACENUMBER;
    int LANGUAGE;

    TextView placeName;
    TextView info;
    TextView stamp;
    TextView isvisitCheck;
    TextView picture;
    ImageView placeImage;
    ImageView isvisitStamp;
    ImageView newimage;
    ImageView image_1;
    ImageView image_2;
    ImageView image_3;


    TextView temparature;
    TextView humidity;
    TextView finedust;
    TextView ozon;

    SharedPrefManager pref;

    ImageView.OnClickListener ivonClickListener;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_place);

        pref = SharedPrefManager.getInstance(this);
        LANGUAGE = pref.getPrefDataLanguage();

        Intent intent = getIntent();
        PLACENUMBER = intent.getExtras().getInt("PLACENUMBER");


        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);

        placeName       = (TextView)findViewById(R.id.tv_aboutplace_placeName);
        info             = (TextView)findViewById(R.id.tv_aboutplace_info);
        stamp            = (TextView)findViewById(R.id.tv_aboutplace_stamp);
        isvisitCheck    = (TextView)findViewById(R.id.tv_aboutplace_isvisittext);
        picture          = (TextView)findViewById(R.id.tv_aboutplace_picture);
        placeImage      = (ImageView)findViewById(R.id.iv_aboutplace_placeImage);
        isvisitStamp    = (ImageView)findViewById(R.id.iv_aboutplace_isvisitedstamp);
        newimage        = (ImageView)findViewById(R.id.iv_aboutplace_newimage);
        image_1         = (ImageView)findViewById(R.id.iv_aboutplace_image_1);
        image_2         = (ImageView)findViewById(R.id.iv_aboutplace_image_2);
        image_3         = (ImageView)findViewById(R.id.iv_aboutplace_image_3);

        temparature   = (TextView)findViewById(R.id.tv_aboutplace_temp);
        humidity       = (TextView)findViewById(R.id.tv_aboutplace_humi);
        finedust       = (TextView)findViewById(R.id.tv_aboutplace_finedust);
        ozon            = (TextView)findViewById(R.id.tv_aboutplace_ozon);

        parsingWeatherData(); //비콘을 통하여 얻어온 (아두이노의) 날씨 데이터를 파싱하여 셋팅
        setPlaceSetting(); //장소 번호 (PLACENUM)를 통해 장소에 해당하는 내용으로 셋팅
        onClickListenerfromUserImage(); //image_1, image_2, image_3의 onClickListener를 셋팅
        setImage(); //유저가 촬영하였거나, 선택하였던 사진들을 불러와 하단의 이미지로 보여줌

        //화면을 새로고침 하는 부분
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setImage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void parsingWeatherData(){
        String Major = Constants.WEATHER.Major;
        String Minor = Constants.WEATHER.Minor;
        ozon.setText(Major.substring(2,3));
        finedust.setText(Integer.parseInt(Major.substring(4,6),16)+"㎍/m³");
        temparature.setText(Integer.parseInt(Minor.substring(2,4),16)+"℃");
        humidity.setText(Integer.parseInt(Minor.substring(4,6),16)+"%");
    }

    public void setPlaceSetting(){
        String placestr = new String();
        String[] strArrayofPlace;
        switch (PLACENUMBER){
            case Constants.PLACENUMBER.NAKSANPARK:
                placestr += "naksanpark";
                strArrayofPlace = getResources().getStringArray(getResources().getIdentifier(placestr,"array",getPackageName()));
                if(Constants.UserVisit.isNaksanpark()) { visitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                else{ nonvisitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                break;
            case Constants.PLACENUMBER.NAKSANROAD:
                placestr += "naksanroad";
                strArrayofPlace = getResources().getStringArray(getResources().getIdentifier(placestr,"array",getPackageName()));
                if(Constants.UserVisit.isNaksanroad()){ visitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                else{ nonvisitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                break;
            case Constants.PLACENUMBER.HANSUNGUNI:
                placestr += "hansunguni";
                strArrayofPlace = getResources().getStringArray(getResources().getIdentifier(placestr,"array",getPackageName()));
                if(Constants.UserVisit.isHansunguni()){ visitPlace(placestr,strArrayofPlace[LANGUAGE]);}
                else{ nonvisitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                break;
            case Constants.PLACENUMBER.HYEHWADOOR:
                placestr += "hyehwadoor";
                strArrayofPlace = getResources().getStringArray(getResources().getIdentifier(placestr,"array",getPackageName()));
                if(Constants.UserVisit.isHyehwadoor()){ visitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                else{ nonvisitPlace(placestr,strArrayofPlace[LANGUAGE]); }
                break;
        }

        strArrayofPlace = getResources().getStringArray(getResources().getIdentifier(placestr,"array",getPackageName()));
        placeName.setText(strArrayofPlace[LANGUAGE]);
        placeImage.setImageResource(getResources().getIdentifier("home_place_"+placestr+"_image", "mipmap",getPackageName()));

        String Info[]       = getResources().getStringArray(R.array.aboutplace_info);
        String Stamp[]      = getResources().getStringArray(R.array.aboutplace_stamp);
        String Picture[]    = getResources().getStringArray(R.array.aboutplace_picture);

        info.setText(Info[LANGUAGE]);
        stamp.setText(Stamp[LANGUAGE]);
        picture.setText(Picture[LANGUAGE]);
    }

    private void nonvisitPlace(String placestr, String placeName){
        isvisitCheck.setText("'"+placeName+"' "+"미방문");
        isvisitStamp.setImageResource(getResources().getIdentifier("home_stamp_nonvisit_"+placestr,"mipmap", getPackageName()));
    }

    private void visitPlace(String placestr, String placeName){
        isvisitCheck.setText("'"+placeName+"' "+"방문");
        isvisitCheck.setTextColor(getResources().getColor(R.color.colorBlack_80));
        isvisitStamp.setImageResource(getResources().getIdentifier("home_stamp_visit_"+placestr,"mipmap", getPackageName()));
        newimage.setImageResource(getResources().getIdentifier("home_user_upload_"+placestr, "mipmap", getPackageName()));
    }

    public void onClickedPlaceImage(View view){
        AboutPlaceImageDialog ImageDialog;
        ImageDialog = new AboutPlaceImageDialog(AboutPlaceActivity.this);
        ImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageDialog.PlaceImageset(PLACENUMBER);
        ImageDialog.show();
    }

    public void onClickedPictureImage(View view){
        FragmentManager fm = getSupportFragmentManager();
        AboutPlaceCameraDialog CameraDialog;
        CameraDialog = new AboutPlaceCameraDialog();
        CameraDialog.setPlaceNumber(PLACENUMBER);
        CameraDialog.show(fm, "camera_dialog");
    }

    public void onClickedUserImage(Bitmap bitmap){
        AboutPlaceImageDialog ImageDialog;
        ImageDialog = new AboutPlaceImageDialog(AboutPlaceActivity.this);
        ImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageDialog.UserImageset(bitmap);
        ImageDialog.show();
    }

    private void onClickListenerfromUserImage(){
        ivonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap;
                switch (view.getId()){
                    case R.id.iv_aboutplace_image_1:
                        bitmap = ((BitmapDrawable)image_1.getDrawable()).getBitmap();
                        onClickedUserImage(bitmap);
                        break;
                    case R.id.iv_aboutplace_image_2:
                        bitmap = ((BitmapDrawable)image_2.getDrawable()).getBitmap();
                        onClickedUserImage(bitmap);
                        break;
                    case R.id.iv_aboutplace_image_3:
                        bitmap = ((BitmapDrawable)image_3.getDrawable()).getBitmap();
                        onClickedUserImage(bitmap);
                        break;
                    default:
                        bitmap = ((BitmapDrawable)placeImage.getDrawable()).getBitmap();
                        onClickedUserImage(bitmap);
                }
            }
        };
    }

    private void setImage() {
        if(getImageFilesFromGallery()!=null) {
            ArrayList<File> files = getImageFilesFromGallery();
            if(files.size() != 0) {
                for (int i = 0; i < files.size(); i++) {
                    File imgFile = files.get(i);
                    if (imgFile.exists()) {
                        if(i < 3) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            int resID = getResources().getIdentifier("iv_aboutplace_image_" + (i + 1), "id", getPackageName());
                            ((ImageView) findViewById(resID)).setImageBitmap(bitmap);
                            ((ImageView) findViewById(resID)).setOnClickListener(ivonClickListener);
                        }else {
                            if ((i % 2) == 1) {
                                LinearLayout addlayout = (LinearLayout) findViewById(R.id.ll_aboutplace_addphoto);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                LinearLayout linLayout = (LinearLayout)inflater.inflate(R.layout.layout_photoadd, addlayout, true);
                                ImageView imageView1 = (ImageView)linLayout.findViewById(R.id.iv_aboutplace_image_4);
                                imageView1.setId(imgviewid + i);
                                imageView1.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                                userImageClickListenter(imageView1);
                            } else {
                                ImageView imageView2 = (ImageView)findViewById(R.id.iv_aboutplace_image_5);
                                imageView2.setId(imgviewid + i);
                                imageView2.setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
                                userImageClickListenter(imageView2);
                            }
                        }
                    }
                }
            }
        }
    }

    private void userImageClickListenter(final ImageView imageView){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap;
                bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                onClickedUserImage(bitmap);
            }
        });
    }

    private ArrayList<File> getImageFilesFromGallery(){
        ArrayList<File> imageFile = null;
        switch (PLACENUMBER){
            case Constants.PLACENUMBER.NAKSANROAD:
                if(pref.getPrefDataNaksanroadimage()!=null) {
                    imageFile = strArraytoFiles(pref.getPrefDataNaksanroadimage());
                }
                break;
            case Constants.PLACENUMBER.NAKSANPARK:
                if(pref.getPrefDataNaksanparkimage()!=null) {
                    imageFile = strArraytoFiles(pref.getPrefDataNaksanparkimage());
                }
                break;
            case Constants.PLACENUMBER.HANSUNGUNI:
                if(pref.getPrefDataHansunguniimage()!=null) {
                    imageFile = strArraytoFiles(pref.getPrefDataHansunguniimage());
                }
                break;
            case Constants.PLACENUMBER.HYEHWADOOR:
                if(pref.getPrefDataHyehwadoorimage()!=null) {
                    imageFile = strArraytoFiles(pref.getPrefDataHyehwadoorimage());
                }
                break;
        }
        return imageFile;
    }

    private ArrayList<File> strArraytoFiles(ArrayList<String> urls){
        ArrayList<File> imgFiles = new ArrayList<>();
        for(int i=0;i<urls.size();i++){
            File file = new File(urls.get(i));
            imgFiles.add(file);
        }
        return imgFiles;
    }

    @Override
    protected void onDestroy() {
        image_1.setImageBitmap(null);
        image_2.setImageBitmap(null);
        image_3.setImageBitmap(null);
        super.onDestroy();
    }
}
