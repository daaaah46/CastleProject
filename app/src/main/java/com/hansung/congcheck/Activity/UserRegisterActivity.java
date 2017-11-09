package com.hansung.congcheck.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import com.hansung.congcheck.R;

public class UserRegisterActivity extends AppCompatActivity {

    EditText phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        phoneNumber = (EditText)findViewById(R.id.et_register_phoneNumber);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String userNumber = "";
        try{
            userNumber = telephonyManager.getLine1Number();
        }catch (Exception e){

        }
        phoneNumber.setText("0"+userNumber);
    }
}
