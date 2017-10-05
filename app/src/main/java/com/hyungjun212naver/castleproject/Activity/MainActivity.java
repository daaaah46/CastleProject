package com.hyungjun212naver.castleproject.Activity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyungjun212naver.castleproject.Fragment.HomeFragment;
import com.hyungjun212naver.castleproject.Fragment.Option1Fragment;
import com.hyungjun212naver.castleproject.Fragment.Option2Fragment;
import com.hyungjun212naver.castleproject.Fragment.Option3Fragment;
import com.hyungjun212naver.castleproject.Fragment.Option4Fragment;
import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.Constants;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class MainActivity extends AppCompatActivity implements BeaconConsumer{

    /**
     * Navigation UI, Variables setting
     */
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    public static int navItemIndex = 0;

    public static int countBackFlag;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backpressedTime = 0;
    private String[] activityTitles;
    private Handler mHandler;

    public static String LOGINSTATE = Constants.LOGIN.LOGINFAIL;
    private static String USER_ID = null;
    private static String LOGIN_ID;

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
    private static final String UUID4 = "aaaaaaaa-bbbb-bbbb-cccc-cccc00000021";

    /**
     * Log Tag
     */
    private static final String TAG_HOME = "home";
    private static final String TAG_OPT_1 = "option_1";
    private static final String TAG_OPT_2 = "option_2";
    private static final String TAG_OPT_3 = "option_3";
    private static final String TAG_OPT_4 = "option_4";
    public static String CURRENT_TAG = TAG_HOME;

//    public static String CSL = Constants.DATABASE.CUSTOMERSRCHLIST_N;
//    public static ArrayList<CustomerSrchList.CustomerSrchListData> customerSrchListDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Constants.UserVisit.isGetServerData()) {
            getServerData();
        }
        initMainActivity(savedInstanceState);
    }

    private void initMainActivity(Bundle savedInstanceState){

        countBackFlag = 0;

        Log.e("MainActivity", "start");

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    public void loadHomeFragment() {

        selectNavMenu();
        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 1:
                Option1Fragment option1Fragment = new Option1Fragment();
                return option1Fragment;

            case 2:
                Option2Fragment option2Fragment = new Option2Fragment();
                return option2Fragment;

            case 3:
                Option3Fragment option3Fragment = new Option3Fragment();
                return option3Fragment;

            case 4:
                Option4Fragment option4Fragment = new Option4Fragment();
                return option4Fragment;

            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        toolbar.setTitle(activityTitles[navItemIndex]);

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_option_1:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_OPT_1;
                        break;
                    case R.id.nav_option_2:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_OPT_2;
                        break;
                    case R.id.nav_option_3:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_OPT_3;
                        break;
                    case R.id.nav_option_4:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_OPT_4;
                        break;
                    default:
                        navItemIndex = 0;
                }

                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };


        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if(CURRENT_TAG != TAG_HOME){
                homeButtonClicked(R.id.nav_home);
            } else {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backpressedTime;

                if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                    LOGINSTATE = Constants.LOGIN.LOGINFAIL;
                    super.onBackPressed();
                } else {
                    backpressedTime = tempTime;
                    Toast.makeText(getApplicationContext(), "한번 더 뒤로가기를 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void homeButtonClicked(int btn_id){
        switch (btn_id) {

            case R.id.nav_home:
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                break;
            case R.id.nav_option_1:
                navItemIndex = 1;
                CURRENT_TAG = TAG_OPT_1;
                break;
            case R.id.nav_option_2:
                navItemIndex = 2;
                CURRENT_TAG = TAG_OPT_2;
                break;
            case R.id.nav_option_3:
                navItemIndex = 3;
                CURRENT_TAG = TAG_OPT_3;
                break;
            case R.id.nav_option_4:
                navItemIndex = 4;
                CURRENT_TAG = TAG_OPT_4;
                break;
            default:
                navItemIndex = 0;
        }

        loadHomeFragment();
    }

    public void opt2clicked(){
        navItemIndex = 1;
        CURRENT_TAG = TAG_OPT_1;
        loadHomeFragment();
    }

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
