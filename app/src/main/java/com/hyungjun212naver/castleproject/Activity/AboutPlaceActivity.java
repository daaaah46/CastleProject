package com.hyungjun212naver.castleproject.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hyungjun212naver.castleproject.R;

public class AboutPlaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_place);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        //인텐트 값에 따라서 보여지는 바의 이름 달라지게끔

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "카메라를 실행합니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
    }

}
