package com.hansung.congcheck.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.SharedPrefManager;

public class SettingFragment extends Fragment {
    /**
     * UI Connect
     */
    Switch switch_popup;
    LinearLayout version;
    LinearLayout notice;
    LinearLayout language;
    LinearLayout.OnClickListener onClickListener;

    boolean setPopup;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SharedPrefManager pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_setting, container, false);

        //preference 데이터 가져옴
        pref = SharedPrefManager.getInstance(getActivity().getApplicationContext());

        version = (LinearLayout)view.findViewById(R.id.ll_setting_version);
        notice = (LinearLayout)view.findViewById(R.id.ll_setting_notice);
        language = (LinearLayout)view.findViewById(R.id.ll_setting_language);
        setOnClickListener();
        version.setOnClickListener(this.onClickListener);
        notice.setOnClickListener(this.onClickListener);
        language.setOnClickListener(this.onClickListener);
        switch_popup = (Switch)view.findViewById(R.id.switch_popup);
        getConstantData();
        switch_popup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    Snackbar.make(container, "팝업 설정을 On 하였습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    setPopup = true;

                }else{
                    Snackbar.make(getView(), "팝업 설정을 Off 하였습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    setPopup = false;
                }
            }
        });
        return view;
    }

    private void setOnClickListener(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.ll_setting_version:
                        fragmentTransaction.replace(R.id.frameLay_home_layout, new SettingVersionFragment());
                        fragmentTransaction.addToBackStack(null); //addToBackStack()을 사용하면 이전 Fragment로 돌아가기 가능 (프래그먼트를 스택처럼 쌓는다!)
                        fragmentTransaction.commit();
                        break;
                    case R.id.ll_setting_notice:
                        fragmentTransaction.replace(R.id.frameLay_home_layout, new SettingNoticeFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.ll_setting_language:
                        fragmentTransaction.replace(R.id.frameLay_home_layout, new SettingLanguageFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
            }
        };
    }

    @Override
    public void onPause(){
        super.onPause();
        pref.setPrefDataPopup(setPopup);
    }

    //기존에 설정된 데이터를 가져온다.
    private void getConstantData(){
        setPopup = pref.getPrefDataPopup();
        switch_popup.setChecked(setPopup);
    }
}