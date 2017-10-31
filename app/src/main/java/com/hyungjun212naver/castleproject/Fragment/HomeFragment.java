package com.hyungjun212naver.castleproject.Fragment;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.hyungjun212naver.castleproject.Activity.AboutPlaceActivity;
import com.hyungjun212naver.castleproject.R;


public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    //장소 정의
    private static final LatLng NARKSAN_PARK = new LatLng(37.580466,127.008580);
    private static final LatLng HEAWHAMOON = new LatLng(37.587903,127.003571);
    private static final LatLng HANSUNGUNIV = new LatLng(37.582357,127.011259);


    //마커 정의
    private Marker mNarksan;
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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
         *  m0 : 낙산공원
         *  m1 : 혜화문
         *  m2 : 한성대학교
         */

        switch (marker.getId()){
            case "m0":
                break;
            case "m1":
                Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case "m2":
                Intent intent = new Intent(getActivity(), AboutPlaceActivity.class);
                startActivity(intent);
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
                .position(NARKSAN_PARK)
                .title("낙산공원(Naksan Park)")
                .snippet("서울시 종로구 동숭동 낙산길 54"));
        mNarksan.showInfoWindow();

        mHeawha = mMap.addMarker(new MarkerOptions()
                .position(HEAWHAMOON)
                .title("혜화문")
                .snippet("서울시 성북구 성북동1가 1-1")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));

        mHansungUNIV = mMap.addMarker(new MarkerOptions()
                .position(HANSUNGUNIV)
                .title("흥인지문")
                .snippet("서울시 종로구 종로 5.6가동 종로 288"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(NARKSAN_PARK));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnInfoWindowClickListener(this);
        onMapaddPolyline(mMap);
    }

    public void onMapaddPolyline(GoogleMap mMap){

        Double ArrayLines [][] = {
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
        mMap.addPolyline(polylineOptions);
    }
}
