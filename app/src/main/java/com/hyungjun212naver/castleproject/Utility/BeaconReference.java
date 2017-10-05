package com.hyungjun212naver.castleproject.Utility;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.hyungjun212naver.castleproject.Activity.MainActivity;
import com.hyungjun212naver.castleproject.R;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;

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

    /**
     * Region 정의에 필요한 변수 선언
     * UUID# : Region으로 쓸 곳의 비콘 UUID
     */
    ArrayList<Region> regions;
    private static final String UUID1 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000001"; //우리 비콘 UUID
    private static final String UUID2 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000002"; //지금 있는 스벅 UUID
    private static final String UUID3 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000003";
    private static final String UUID4 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000021"; //우리 비콘 UUID

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "앱 시작 in BeaconReference");
        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers()
                .add(new BeaconParser()
                        .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // iBeacon 레이아웃으로 설정
        beaconManager.setBackgroundMode(true);

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
    }

    // Region에 적어도 하나 이상의 비콘이 탐지되면, MonitorNotifier.INSIDE 를호출
    // Region에 비콘이 탐지되지 않으면 MonitorNotifier.OUTSIDE를 호출
    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {
        Log.d(TAG, "didDetermineStateForRegion 함수 호출됨 " + " UUID : " + arg1.getId1() + " Major : " + arg1.getId2() + " Minor : " + arg1.getId3());
        Log.d(TAG, arg0+"");
    }

    // Called when at least one beacon in a Region is visible.
    // Region에 적어도 1개의 비콘이 표시될 때 호출
    @Override
    public void didEnterRegion(Region arg0) {
        Log.d(TAG, "didEnterRegion 함수 호출됨 >>> " + "Region 이름 : "+ arg0.getUniqueId() +" UUID : " + arg0.getId1() + " Major : " + arg0.getId2() + " Minor : " + arg0.getId3());
        sendNotification();
       /*Intent intent = new Intent(this, MainActivity.class);
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);*/
    }

    //Called when no beacons in a Region are visible.
    //Region 내에 비콘이 보이지 않을 경우
    @Override
    public void didExitRegion(Region arg0) {
        Log.d(TAG, "didExitRegion 함수 호출됨 : 안보임");
    }

    //알림창 생성, 추후에 변경
    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Reference Application")
                        .setContentText("An beacon is nearby.")
                        .setSmallIcon(R.mipmap.ic_launcher_round);

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
}
