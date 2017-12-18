package com.hansung.congcheck.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hansung.congcheck.POJO.UserInfoList;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Retrofit2.HttpClient;
import com.hansung.congcheck.Retrofit2.HttpService;
import com.hansung.congcheck.Utility.SharedPrefManager;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisterActivity extends AppCompatActivity {
    String TAG = "UserRegisterActivity";

    EditText phoneNumber;
    Button registerConfirm;
    Button.OnClickListener onClickListener;

    List<UserInfoList.UserInfo> userInfo;
    SharedPrefManager pref;
    String userNumber = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = SharedPrefManager.getInstance(this);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        try {
            userNumber = telephonyManager.getLine1Number();
        } catch (Exception e) {

        }
        Log.e(TAG, userNumber);

        if(pref.getPrefDataUsernumber() != null){
            Intent intent = new Intent(UserRegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            //Retrofit2 동기방식
            new AsyncTask<Void, Void, String>(){
                @Override
                protected String doInBackground(Void... voids) {
                    HttpService api = HttpClient.getStationListService();
                    Call<UserInfoList> call = api.getUserInfo(userNumber);
                    try{
                        userInfo = call.execute().body().getUserInfo();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(userInfo.size() == 0){
                        setContentView(R.layout.activity_user_register);
                        phoneNumber = (EditText) findViewById(R.id.et_register_userphoneNum);
                        registerConfirm = (Button) findViewById(R.id.btn_register_confirm);
                        Toast.makeText(getApplicationContext(), "기존에 없는 사용자 입니다.", Toast.LENGTH_SHORT).show();
                        phoneNumber.setText(userNumber);
                        OnClickListenerSetting();
                        registerConfirm.setOnClickListener(onClickListener);
                        putUserInfo(userNumber);
                    }else{
                        Toast.makeText(getApplicationContext(), "기존에 있는 사용자 입니다.", Toast.LENGTH_SHORT).show();
                        UserPrefRegister();
                    }
                }
            }.execute();
        }
    }

    private void UserPrefRegister(){
        pref.setPrefDataUsernumber(userNumber);
        Intent intent = new Intent(UserRegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void OnClickListenerSetting(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Register User Success!", Toast.LENGTH_SHORT).show();
                UserPrefRegister();
            }
        };
    }

    private void putUserInfo(final String phoneNum){
        HttpService api = HttpClient.getStationListService();
        Call<UserInfoList> call = api.setUserInfo(phoneNum);
        call.enqueue(new Callback<UserInfoList>() {
            @Override
            public void onResponse(Call<UserInfoList> call, Response<UserInfoList> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "데이터베이스에 사용자 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "데이터베이스 사용자 등록 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserInfoList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
