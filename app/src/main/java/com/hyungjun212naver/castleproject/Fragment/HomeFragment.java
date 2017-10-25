package com.hyungjun212naver.castleproject.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyungjun212naver.castleproject.Activity.AboutPlaceActivity;
import com.hyungjun212naver.castleproject.Activity.MainActivity;
import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.Constants;

public class HomeFragment extends Fragment {

    /**
     * ImageButton : 스탬프 이미지 버튼, 버튼 클릭 시 상세정보 확인 가능
     */
    ImageButton imgbtn_place01;
    ImageButton imgbtn_place02;
    ImageButton imgbtn_place03;
    ImageButton imgbtn_place04;

    /**
     * 장소 상세 정보 및 버튼
     */
    TextView tv_home_placeInfo;
    Button btn_home_moreInfo;

    String tag = "HomeFragment";
    int ClickedButton = 1; //초기 값 1번?으로 해야하나?(1번 장소 정보가 기본으로 나옴

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

        /**
         * UI Connect
         */
        imgbtn_place01 = view.findViewById(R.id.imgbtn_place01);
        imgbtn_place02 = view.findViewById(R.id.imgbtn_place02);
        imgbtn_place03 = view.findViewById(R.id.imgbtn_place03);
        imgbtn_place04 = view.findViewById(R.id.imgbtn_place04);
        tv_home_placeInfo = view.findViewById(R.id.tv_home_placeInfo);
        btn_home_moreInfo = view.findViewById(R.id.btn_home_moreInfo);

        setStampImage(); // 방문 여부에 따른 버튼 이미지 셋팅

        imgbtn_place01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = "1번장소 설명";
                tv_home_placeInfo.setText(info);
                ClickedButton = 1;
            }
        });

        imgbtn_place02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = "2번장소 설명";
                tv_home_placeInfo.setText(info);
                ClickedButton = 2;
            }
        });

        imgbtn_place03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = "3번장소 설명 넣기";
                tv_home_placeInfo.setText(info);
                ClickedButton = 3;
            }
        });

        imgbtn_place04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = "4번장소 설명 넣기";
                tv_home_placeInfo.setText(info);
                ClickedButton = 4;
            }
        });

        btn_home_moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), AboutPlaceActivity.class);
                intent.putExtra("placenum", ClickedButton);
                startActivity(intent);

                /*
                Fragment간 이동 시 이렇게 작성할 것
                 */
                /*Fragment fragment;
                fragment = new Option1Fragment();
                FragmentManager fragmentManager;
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction;
                fragmentTransaction = fragmentManager.beginTransaction();
               fragmentTransaction.replace(R.id.frame,fragment);
                fragmentTransaction.commit();*/
            }
        });
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    /**
     * 방문 여부에 따라 버튼 이미지 셋팅
     */
    public void setStampImage(){
        if(Constants.UserVisit.isPlace01()){
            imgbtn_place01.setBackgroundResource(R.mipmap.ic_launcher_round);
        }
        if(Constants.UserVisit.isPlace02()){
            imgbtn_place02.setBackgroundResource(R.mipmap.ic_launcher_round);
        }
        if(Constants.UserVisit.isPlace03()){
            imgbtn_place03.setBackgroundResource(R.mipmap.ic_launcher_round);
        }
        if(Constants.UserVisit.isPlace04()){
            imgbtn_place04.setBackgroundResource(R.mipmap.ic_launcher_round);
        }
    }
}
