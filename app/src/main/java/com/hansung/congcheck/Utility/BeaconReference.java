package com.hansung.congcheck.Utility;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.hansung.congcheck.Activity.AboutPlaceActivity;
import com.hansung.congcheck.Activity.MainActivity;
import com.hansung.congcheck.POJO.UserInfoList;
import com.hansung.congcheck.POJO.UserVisitData;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Retrofit2.HttpClient;
import com.hansung.congcheck.Retrofit2.HttpService;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by daaaaah on 2017-10-06.
 */

public class BeaconReference extends Application implements BootstrapNotifier {
    private static final String TAG = "BeaconReference";
    /**
     * AltBeacon 기본 값 셋팅
     */
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    SharedPrefManager pref;

    List<UserInfoList.UserInfo> userInfo;
    String RegionID;
    Region curRegion;

    List<UserVisitData.VisitValue> userVisitValue;

    /**
     * Region 정의에 필요한 변수 선언
     * UUID# : Region으로 쓸 곳의 비콘 UUID
     */
    ArrayList<Region> regions;
    private static final String UUID1 = "AAAAAAAA-BBBB-BBBB-CCCC-CCCC00000001";
    private static final String UUID2 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000002";
    private static final String UUID3 = "8fef2e11-d140-2ed1-2eb1-4138edcabe09"; //테스트용
    private static final String UUID4 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000021";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "앱 시작 in BeaconReference");
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers()
                .add(new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // iBeacon 레이아웃으로 설정
        beaconManager.setBackgroundMode(true);
        beaconManager.setBackgroundScanPeriod(5000); //백그라운드 스캔 주기를 5초로 설정함

        // 비콘이 감지되면 깨운다. ( >> didEnterRegion() 호출)
        // (아래의 파라미터에 특정 id를 지정할 수 있다 >> Identifier.parse(UUID);로 특정 UUID를 설정
        // 세 파라미터를 모두 NULL로 만들면, 모든 비콘이 탐지될 때 마다 didEnterRegion()를 호출한다.
        // Region이 4개 이므로, Region을 4개 생성하여, ArrayList에 add 하는 방식을 사용함
        Region region1 = new Region("Region1", Identifier.parse(UUID1), null,null);
        Region region2 = new Region("Region2",Identifier.parse(UUID2), null,null);
        Region region3 = new Region("Region3",Identifier.parse(UUID3), null,null);
        Region region4 = new Region("Region4",Identifier.parse(UUID4), null,null);
        regions = new ArrayList<Region>();
        regions.add(region1);
        regions.add(region2);
        regions.add(region3);
        regions.add(region4);
        regionBootstrap = new RegionBootstrap(this, regions);
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        pref = SharedPrefManager.getInstance(this);
    }

    // Region에 적어도 하나 이상의 비콘이 탐지되면, MonitorNotifier.INSIDE 를호출
    // Region에 비콘이 탐지되지 않으면 MonitorNotifier.OUTSIDE를 호출
    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {
        //Log.d(TAG, "DetermineStateForRegion 함수 호출됨 " + " UUID : " + arg1.getId1() + " Major : " + arg1.getId2() + " Minor : " + arg1.getId3());
    }

    // Called when at least one beacon in a Region is visible.
    // Region에 적어도 1개의 비콘이 표시될 때 호출
    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "EnterRegion 함수 호출됨 >>> " + "Region 이름 : "+ arg0.getUniqueId() +" UUID : " + arg0.getId1() + " Major : " + arg0.getId2() + " Minor : " + arg0.getId3());
        curRegion = arg0;

        //만약에 사용자가 1시간 이내에 방문을 했다면
        //그냥 넘어감
        //근데 1시간 이후에 방문을 했다면
        //Notification을 띄운다.
        
        NotiandSetVisitedData();
    }

    private int setPlaceNumber(){
        int PlaceNum = Constants.PLACENUMBER.NAKSANROAD; //default
        switch (curRegion.getUniqueId()){
            case "Region1":
                PlaceNum = Constants.PLACENUMBER.NAKSANROAD;
                break;
            case "Region2":
                PlaceNum = Constants.PLACENUMBER.NAKSANPARK;
                break;
            case "Region3":
                PlaceNum = Constants.PLACENUMBER.HANSUNGUNI;
                break;
            case "Region4":
                PlaceNum = Constants.PLACENUMBER.HYEHWADOOR;
                break;
        }
        RegionID = curRegion.getUniqueId();
        return PlaceNum;
    }

    private void NotiandSetVisitedData(){
        if(pref.getPrefDataPopup() == true) { //데이터 팝업을 true로 설정해놓았으면
            sendNotification(); //알림창을 생성한다
            Intent intent = new Intent(this, AboutPlaceActivity.class); //액티비티를 띄우기 위한 준비를 한다
            intent.putExtra("PLACENUMBER", setPlaceNumber()); //intent로 꼭 장소 넘버를 보내줘야 한다.
            setUserVisitedData(pref.getPrefDataUsernumber()); //데이터베이스에 방문 값을 넣습니다..!
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }else{
            setUserVisitedData(pref.getPrefDataUsernumber()); //데이터베이스에 방문 값을 넣습니다..!
        }
    }

    //Called when no beacons in a Region are visible.
    //Region 내에 비콘이 보이지 않을 경우
    @Override
    public void didExitRegion(Region arg0) {
        Log.d(TAG, "didExitRegion 함수 호출됨 : 안보임");
    }

    //알림창 생성
    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Congcheck Alarm")
                        .setContentText("장소 근처에 도착하였습니다.")
                        .setSmallIcon(R.mipmap.ic_launcher);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void setUserVisitedData(String userNumber){
        HttpService api = HttpClient.getStationListService();
        Call<UserInfoList> call = api.getUserInfo(userNumber);
        call.enqueue(new Callback<UserInfoList>() {
            @Override
            public void onResponse(Call<UserInfoList> call, Response<UserInfoList> response) {
                if(response.isSuccessful()){
                    userInfo = response.body().getUserInfo();
                    UserVisitDataSetting(userInfo.get(0).getNum(), RegionID);
                } else {
                    Toast.makeText(getApplicationContext(), "방문데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserInfoList> call, Throwable t) {
                //.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void UserVisitDataSetting(String num,String regionID){
        String locationName = new String();
        switch (regionID){
            case "Region1":
                locationName = "naksanroad";
                break;
            case "Region2":
                locationName = "naksanpark";
                break;
            case "Region3":
                locationName = "hansunguni";
                break;
            case "Region4":
                locationName = "heyhwadoor";
                break;
        }

        HttpService api = HttpClient.getStationListService();
        Call<UserVisitData> call = api.setUservisitInfo(num, locationName);
        call.enqueue(new Callback<UserVisitData>() {
            @Override
            public void onResponse(Call<UserVisitData> call, Response<UserVisitData> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "지역 방문 처리 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "지역 방문 처리 실패", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserVisitData> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}