package com.hansung.congcheck.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hansung.congcheck.Activity.AboutPlaceActivity;
import com.hansung.congcheck.POJO.LocationInfoList;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.Constants;

import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    /**
     *
     */
    String count = "one";
    List<LocationInfoList.LocationInfo> locationInfo;

    //장소 정의
    private static final LatLng NARKSANPARK = new LatLng(37.580466,127.008580);
    private static final LatLng NARKSANROAD = new LatLng(37.581689,127.007439);
    private static final LatLng HYEWHADOOR = new LatLng(37.587903,127.003571);
    private static final LatLng HANSUNGUNI = new LatLng(37.582357,127.011259);

    //마커 정의
    private Marker mNarksanPark;
    private Marker mNarksanRoad;
    private Marker mHyewhaDoor;
    private Marker mHansungUni;

    private GoogleMap mMap;

    public HomeFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        /*
         *  m0 : 낙산공원길
         *  m1 : 혜화문
         *  m2 : 한성대학교
         */

        switch (marker.getId()){
            case "m0":
                Intent intent1 = new Intent(getActivity(), AboutPlaceActivity.class);
                intent1.putExtra("PLACENUMBER", Constants.PLACENUMBER.NAKSANPARK);
                startActivity(intent1);
                break;
            //더넣어!!!
            case "m1":
                Intent intent2 = new Intent(getActivity(), AboutPlaceActivity.class);
                intent2.putExtra("PLACENUMBER", Constants.PLACENUMBER.NAKSANROAD);
                startActivity(intent2);
                break;
            case "m2":
                Intent intent3 = new Intent(getActivity(), AboutPlaceActivity.class);
                intent3.putExtra("PLACENUMBER", Constants.PLACENUMBER.HYEHWADOOR);
                startActivity(intent3);
                break;
            case "m3":
                Intent intent4 = new Intent(getActivity(), AboutPlaceActivity.class);
                intent4.putExtra("PLACENUMBER", Constants.PLACENUMBER.HANSUNGUNI);
                startActivity(intent4);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mNarksanPark = mMap.addMarker(new MarkerOptions()
                .position(NARKSANPARK)
                .title("낙산공원(Naksan Park)")
                .snippet("서울시 종로구 동숭동 낙산길 54")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_site_naksanpark)));

        mNarksanRoad = mMap.addMarker(new MarkerOptions()
                .position(NARKSANROAD)
                .title("낙산공원 길")
                .snippet("서울시 종로구 동숭동 50-114")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_site_naksanroad)));

        mHyewhaDoor = mMap.addMarker(new MarkerOptions()
                .position(HYEWHADOOR)
                .title("혜화문")
                .snippet("서울시 성북구 성북동1가 1-1")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_site_hyehwadoor)));

        mHansungUni = mMap.addMarker(new MarkerOptions()
                .position(HANSUNGUNI)
                .title("한성대학교")
                .snippet("서울시 성북구 삼선동 삼선교로 16길 116")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_site_hansunguni)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(NARKSANPARK));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnInfoWindowClickListener(this);
    }
}
