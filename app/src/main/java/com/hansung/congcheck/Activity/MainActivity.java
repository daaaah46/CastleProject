package com.hansung.congcheck.Activity;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hansung.congcheck.Fragment.HomeFragment;
import com.hansung.congcheck.Fragment.OtherPlaceFragment;
import com.hansung.congcheck.Fragment.ProfileFragment;
import com.hansung.congcheck.Fragment.SettingFragment;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.Constants;
import com.hansung.congcheck.Utility.SharedPrefManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    public final int REQUEST_ACCESS_COARSE_LOCATION = 1003;
    public String TAG = "MainActivity";

    /**
     * fragment Valueables
     */
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int FragmentID = 1;

    /**
     * UI Connect
     */
    TextView toolbar_title;
    ImageButton navigation_home;
    ImageButton navigation_otherplace;
    ImageButton navigation_profile;
    ImageButton navigation_setting;
    ImageButton.OnClickListener onClickListener;

    ConstraintLayout weathertoolbar;

    /**
     * SharedPreference Value
     */
    SharedPrefManager pref;

    /**
     * Beacon Variables Setting
     */
    BeaconConsumer beaconConsumer;
    private BluetoothAdapter mBluetoothAdapter;
    private BeaconManager beaconManager;
    String  UUID,Major,Minor;

    //UUID값
    private static final String UUID1 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000001"; //우리 비콘 UUID
    private static final String UUID2 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000002";
    private static final String UUID3 = "aaaaaaaa-bbbb-bbbb-cccc-cccc12121212";
    private static final String UUID4 = "8fef2e11-d140-2ed1-2eb1-4138edcabe09";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        beaconSetting();
        if (!Constants.UserVisit.isGetServerData()) {
            getServerData();
            Constants.UserVisit.setGetServerData(true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        weathertoolbar = (ConstraintLayout) findViewById(R.id.toolbar_weather);
        toolbar_title = (TextView) findViewById(R.id.toobar_title);
        setSupportActionBar(toolbar);
        setToolbarTitle();
        initMainActivity();
        setBottomnavImage();
    }

    private void initMainActivity(){
        ChangeFragment();
        navigation_home         = (ImageButton)findViewById(R.id.navigation_home);
        navigation_otherplace  = (ImageButton)findViewById(R.id.navigation_otherplace);
        navigation_profile     = (ImageButton)findViewById(R.id.navigation_profile);
        navigation_setting     = (ImageButton)findViewById(R.id.navigation_setting);

        setOnClickListener(); //리스너 객체 생성

        navigation_home.setOnClickListener(onClickListener);
        navigation_otherplace.setOnClickListener(onClickListener);
        navigation_profile.setOnClickListener(onClickListener);
        navigation_setting.setOnClickListener(onClickListener);
    }

    private void setOnClickListener(){
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.navigation_home:
                        FragmentID = 1;
                        break;
                    case R.id.navigation_otherplace:
                        FragmentID = 2;
                        break;
                    case R.id.navigation_profile:
                        FragmentID = 3;
                        break;
                    case R.id.navigation_setting:
                        FragmentID = 4;
                        break;
                }
                ChangeFragment();
                setToolbarTitle();
                setBottomnavImage();
            }
        };
    }

    private void ChangeFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (FragmentID){
            case 1 :
                weathertoolbar.setVisibility(View.VISIBLE);
                fragmentTransaction.replace(R.id.frameLay_home_layout, new HomeFragment());
                fragmentTransaction.commit();
                break;
            case 2 :
                fragmentTransaction.replace(R.id.frameLay_home_layout, new OtherPlaceFragment());
                fragmentTransaction.commit();
                weathertoolbar.setVisibility(View.GONE);
                break;
            case 3 :
                fragmentTransaction.replace(R.id.frameLay_home_layout,new ProfileFragment());
                fragmentTransaction.commit();
                weathertoolbar.setVisibility(View.GONE);
                break;
            case 4:
                fragmentTransaction.replace(R.id.frameLay_home_layout,new SettingFragment());
                fragmentTransaction.commit();
                weathertoolbar.setVisibility(View.GONE);
                break;
        }
    }

    //SettingLanguage, Notice, Version에서도 사용해야 해서 public으로 선언
    private void setToolbarTitle(){
        String titleArray[] =  getResources().getStringArray(R.array.nav_item_activity_titles);
        toolbar_title.setText(titleArray[FragmentID-1]); //Fragment 번호를 1부터 시작하게 만들어놔서 -1 해줌
    }

    private void setBottomnavImage(){
        switch (FragmentID){
            case 1:
                navigation_home.setImageResource(R.mipmap.base_home_sel);
                navigation_otherplace.setImageResource(R.mipmap.base_place_nonsel);
                navigation_profile.setImageResource(R.mipmap.base_stamp_nonsel);
                navigation_setting.setImageResource(R.mipmap.base_setting_nonsel);
                break;
            case 2:
                navigation_home.setImageResource(R.mipmap.base_home_nonsel);
                navigation_otherplace.setImageResource(R.mipmap.base_place_sel);
                navigation_profile.setImageResource(R.mipmap.base_stamp_nonsel);
                navigation_setting.setImageResource(R.mipmap.base_setting_nonsel);
                break;
            case 3:
                navigation_home.setImageResource(R.mipmap.base_home_nonsel);
                navigation_otherplace.setImageResource(R.mipmap.base_place_nonsel);
                navigation_profile.setImageResource(R.mipmap.base_stamp_sel);
                navigation_setting.setImageResource(R.mipmap.base_setting_nonsel);
                break;
            case 4:
                navigation_home.setImageResource(R.mipmap.base_home_nonsel);
                navigation_otherplace.setImageResource(R.mipmap.base_place_nonsel);
                navigation_profile.setImageResource(R.mipmap.base_stamp_nonsel);
                navigation_setting.setImageResource(R.mipmap.base_setting_sel);
                break;
        }
    }

    /**
     * 서버에서 유저의 방문 데이터를 가져와서 전역변수에 저장
     */
    public void getServerData(){
        Constants.UserVisit.setNaksanroad(true);
        Constants.UserVisit.setNaksanpark(false);
        Constants.UserVisit.setHansunguni(false);
        Constants.UserVisit.setHyehwadoor(true);
    }

    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //permission check
            int permissionCheck = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            //권한이 부여되지 않았을 경우 : -1(PERMISSION_DENIED). 부여된 경우 0(PERMISSION_GRANTED)
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_ACCESS_COARSE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(),"권한 사용을 동의 해야 블루투스 서비스를 받을 수 있습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    void beaconSetting(){
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add((new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")));
        //현재 5초로 스캔 시간 설정 > 나중에 2분으로 변경!
        beaconManager.setForegroundBetweenScanPeriod(5000);
        beaconManager.bind(this);
    }

    /**
     * BeaconConsumer
     */
    @Override
    protected  void onDestroy(){
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.e(TAG, "--------------------------------------------------------------------------------");
                    Log.e(TAG, "Distance : " + beacons.iterator().next().getDistance() + "(meter)");
                    UUID = beacons.iterator().next().getId1().toString();
                    Log.e(TAG, "UUID : " + UUID);
                    Major = beacons.iterator().next().getId2().toString();
                    Log.i(TAG, "Major : " + Major);
                    Minor = beacons.iterator().next().getId3().toString();
                    Log.i(TAG, "Minor : " + Minor);
                    Log.e(TAG, "--------------------------------------------------------------------------------");
                }
            }
        });
        try{
            beaconManager.startRangingBeaconsInRegion(new Region("Region1", Identifier.parse(UUID1), null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("Region2", Identifier.parse(UUID2), null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("Region3", Identifier.parse(UUID3), null, null));
            beaconManager.startRangingBeaconsInRegion(new Region("Region4", Identifier.parse(UUID4), null, null));
        }catch(RemoteException e){

        }
    }
}
