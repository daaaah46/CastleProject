package com.hyungjun212naver.castleproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.Constants;

import java.util.ArrayList;

public class AboutPlaceActivity extends AppCompatActivity {

    Toolbar toolbar;

    GridView gridView;
    ArrayList<String> imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about_place);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setLanguage();

        setSupportActionBar(toolbar);

        gridView = (GridView)findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter1());

        imgList = new ArrayList<String>();

        //gridView.setAdapter(new ImageAdapter(this));



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "카메라를 실행합니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(AboutPlaceActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    void setLanguage(){
        String[] titleArray = getResources().getStringArray(R.array.About_Place_titles);
        switch (Constants.Language.getLanguage()) {
            case R.id.rB_korean:
                toolbar.setTitle(titleArray[0]);
                break;
            case R.id.rB_english:
                toolbar.setTitle(titleArray[1]);
                break;
            case R.id.rB_japanese:
                toolbar.setTitle(titleArray[2]);
                break;
            case R.id.rB_chinese:
                toolbar.setTitle(titleArray[3]);
                break;
            default:
                toolbar.setTitle(titleArray[0]);
                break;
        }
    }

    public class ImageAdapter extends BaseAdapter{
        private Context context;

        public ImageAdapter(Context con){
            this.context = con;
        }
        public int getCount(){
            return 12;
        }
        public Object getItem(int pos){
            return null;
        }
        public long getItemId(int pos){
            return 0;
        }

        public View getView(int pos, View convertView, ViewGroup parent){
            ImageView imageView;
            if(convertView == null){
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(60,60));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(5,5,5,5);
            }else{
                imageView = (ImageView)convertView;
            }
            imageView.setImageResource(R.mipmap.ic_launcher);
            return imageView;
        }
    }

    public class ImageAdapter1 extends BaseAdapter{
        LayoutInflater inflater;

        public ImageAdapter1(){
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        public int getCount(){
            return 21;
        }
        public Object getItem(int pos){
            return null;
        }
        public long getItemId(int pos){
            return 0;
        }

        public View getView(int pos, View convertView, ViewGroup parent){
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.item, parent, false);
            }
            return  convertView;
        }
    }
}
