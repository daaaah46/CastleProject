package com.hansung.congcheck.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hansung.congcheck.Activity.AboutPlaceActivity;
import com.hansung.congcheck.POJO.LocationAirvalueList;
import com.hansung.congcheck.POJO.LocationInfoList;
import com.hansung.congcheck.R;
import com.hansung.congcheck.Retrofit2.HttpClient;
import com.hansung.congcheck.Retrofit2.HttpService;
import com.hansung.congcheck.Utility.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    /**
     *
     */
    String count = "one";
    List<LocationAirvalueList.LocationAirvalue> locationAirvalue;
    List<LocationInfoList.LocationInfo> locationInfo;

    //장소 정의
    private static final LatLng NARKSANPARK = new LatLng(37.580466,127.008580);
    private static final LatLng NARKSANPARKPATH = new LatLng(37.581689,127.007439);
    private static final LatLng HEAWHAMOON = new LatLng(37.587903,127.003571);
    private static final LatLng HANSUNGUNIV = new LatLng(37.582357,127.011259);

    //마커 정의
    private Marker mNarksan;
    private Marker mNarksanPath;
    private Marker mHeawha;
    private Marker mHansungUNIV;

    private GoogleMap mMap;

    private OnFragmentInteractionListener mListener;

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


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                intent2.putExtra("PLACENUMBER", Constants.PLACENUMBER.HANSUNGUNIV);
                startActivity(intent2);
                break;
            case "m2":
                Intent intent3 = new Intent(getActivity(), AboutPlaceActivity.class);
                intent3.putExtra("PLACENUMBER", Constants.PLACENUMBER.HYEHWAMOON);
                startActivity(intent3);
                break;
            case "m3":
                Intent intent4 = new Intent(getActivity(), AboutPlaceActivity.class);
                intent4.putExtra("PLACENUMBER", Constants.PLACENUMBER.NAKSANPARKPATH);
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
        mNarksan = mMap.addMarker(new MarkerOptions()
                .position(NARKSANPARK)
                .title("낙산공원(Naksan Park)")
                .snippet("서울시 종로구 동숭동 낙산길 54"));
        mNarksan.showInfoWindow();

        mNarksanPath = mMap.addMarker(new MarkerOptions()
                .position(NARKSANPARKPATH)
                .title("낙산공원 길")
                .snippet("서울시 종로구 동숭동 50-114"));

        mHeawha = mMap.addMarker(new MarkerOptions()
                .position(HEAWHAMOON)
                .title("혜화문")
                .snippet("서울시 성북구 성북동1가 1-1")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));

        mHansungUNIV = mMap.addMarker(new MarkerOptions()
                .position(HANSUNGUNIV)
                .title("한성대학교")
                .snippet("서울시 성북구 삼선동 삼선교로 16길 116"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(NARKSANPARK));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnInfoWindowClickListener(this);
        onMapaddPolyline(mMap);
    }

    public void onMapaddPolyline(GoogleMap mMap){

       /* Double ArrayLines [][] = {
                 {37.582357, 127.011259}
                ,{37.582406, 127.012288}
                ,{37.581474, 127.012235}
                ,{37.581499, 127.011972}
                ,{37.581414, 127.011567}};
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        for(int i=0;i<ArrayLines.length;i++){
            LatLng temp = new LatLng(ArrayLines[i][0], ArrayLines[i][1]);
            polylineOptions.add(temp);
        }
        mMap.addPolyline(polylineOptions);*/
    }

    private void searchLocationInfo(){
        HttpService api = HttpClient.getStationListService();
        Call<LocationInfoList> call = api.getLocation_Info();
        call.enqueue(new Callback<LocationInfoList>() {
            @Override
            public void onResponse(Call<LocationInfoList> call, Response<LocationInfoList> response) {

                if(response.isSuccessful()){

                    locationInfo = response.body().getLocationInfo();

                    String responseData = "";

                    for(int i=0; i<locationInfo.size(); i++){

                        responseData += "location_id : "+locationInfo.get(i).getLocatinId()+
                                "\nlocation_name : "+locationInfo.get(i).getLocationName()+
                                "\nlocation_latitude : "+locationInfo.get(i).getLocationLatitude()+
                                "\nlocation_longitude : "+locationInfo.get(i).getLocationLongitude()+
                                "\nlocation_addr : "+locationInfo.get(i).getLocationAddr()+"\n\n";
                    }
                    Log.e("searchLocationInfo", responseData);
                } else {
                    Toast.makeText(getContext(), "worng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LocationInfoList> call, Throwable t) {
                Toast.makeText(getContext(), "connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
