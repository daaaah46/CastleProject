package com.hyungjun212naver.castleproject.Activity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hyungjun212naver.castleproject.Fragment.HomeFragment;
import com.hyungjun212naver.castleproject.Fragment.Option1Fragment;
import com.hyungjun212naver.castleproject.Fragment.Option2Fragment;
import com.hyungjun212naver.castleproject.Fragment.Option3Fragment;
import com.hyungjun212naver.castleproject.Fragment.SettingFragment;
import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.Constants;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class MainActivity extends AppCompatActivity implements BeaconConsumer{
    public static String LOGINSTATE = Constants.LOGIN.LOGINFAIL;
    private static String USER_ID = null;
    private static String LOGIN_ID;

    /**
     * UI Setting
     */
    Button btn_home;
    Button btn_setting;


    /**
     * fragment Valueables
     */
    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int FragmentID = 1;


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

    /**
     * Log Tag
     */
    private static final String TAG_HOME = "home";
    private static final String TAG_OPT_1 = "option_1";
    private static final String TAG_OPT_2 = "option_2";
    private static final String TAG_OPT_3 = "option_3";
    private static final String TAG_OPT_4 = "option_4";
    public static String CURRENT_TAG = TAG_HOME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconSetting();
        if(!Constants.UserVisit.isGetServerData()) {
            getServerData();
        }
        initMainActivity(savedInstanceState);
    }

    private void initMainActivity(Bundle savedInstanceState){
        setContentView(R.layout.activity_main);
        ChangeFragment();
        btn_home = (Button)findViewById(R.id.btn_home_homebtn);
        btn_setting = (Button)findViewById(R.id.btn_home_setting);

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentID = 1;
                ChangeFragment();
            }
        });

        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentID = 4;
                ChangeFragment();
            }
        });
    }


    private void ChangeFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (FragmentID){
            case 1 :
                fragment = new HomeFragment();
                fragmentTransaction.replace(R.id.frameLay_home_layout,fragment);
                fragmentTransaction.commit();
                break;
            case 2 :
                break;
            case 3 :
                break;
            case 4:
                fragment = new SettingFragment();
                fragmentTransaction.replace(R.id.frameLay_home_layout,fragment);
                fragmentTransaction.commit();
                break;
        }
    }
    /*
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        toolbar.setTitle(activityTitles[navItemIndex]);

    }*/


    /**
     * 서버에서 유저의 방문 데이터를 가져와서 전역변수에 저장
     */
    public void getServerData(){
        //서버에서 데이터 가져오는 코드
        Constants.UserVisit.setPlace01(true);
        Constants.UserVisit.setPlace02(false);
        Constants.UserVisit.setPlace03(false);
        Constants.UserVisit.setPlace04(true);
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
                    Log.e(CURRENT_TAG, "--------------------------------------------------------------------------------");
                    Log.e(CURRENT_TAG, "Distance : " + beacons.iterator().next().getDistance() + "(meter)");
                    UUID = beacons.iterator().next().getId1().toString();
                    Log.e(CURRENT_TAG, "UUID : " + UUID);
                    Major = beacons.iterator().next().getId2().toString();
                    Log.i(CURRENT_TAG, "Major : " + Major);
                    Minor = beacons.iterator().next().getId3().toString();
                    Log.i(CURRENT_TAG, "Minor : " + Minor);
                    Log.e(CURRENT_TAG, "--------------------------------------------------------------------------------");
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
