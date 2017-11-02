package com.hyungjun212naver.castleproject.Fragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.Constants;

public class SettingFragment extends Fragment {

    public static final String PREF_DATA_NAME = "DATA_OF_USER_CONGCHECK";
    public static final String PREF_DATA_POP_UP = "POP_UP";
    public static final String PREF_DATA_LANGUAGE = "LANGUAGE";
    public static final String PREF_DATA_APP_VERSION = "APP_VERSION";

    RadioGroup rG_language;
    Switch switch_popup;

    boolean setPopup;


    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_setting, container, false);

        rG_language = (RadioGroup)view.findViewById(R.id.rG_language);
        switch_popup = (Switch)view.findViewById(R.id.switch_popup);
        getConstantData();

        rG_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rG_language.getCheckedRadioButtonId();
                Snackbar.make(container, "언어설정을 저장하였습니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        switch_popup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    Snackbar.make(container, "팝업 설정을 On 하였습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    setPopup = true;

                }else{
                    Snackbar.make(container, "팝업 설정을 Off 하였습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    setPopup = false;
                }
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
    public void onPause(){
        super.onPause();

        SharedPreferences pref = this.getActivity().getSharedPreferences(PREF_DATA_NAME, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean(PREF_DATA_POP_UP,setPopup);
        edit.commit();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    //기존에 설정된 데이터 (Constants)를 가져온다.
    private void getConstantData(){
        SharedPreferences pref = this.getActivity().getSharedPreferences(PREF_DATA_NAME, 0);
        switch_popup.setChecked(pref.getBoolean(PREF_DATA_POP_UP,false));
    }
}