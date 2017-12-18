package com.hansung.congcheck.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hansung.congcheck.R;

/**
 * Created by user5 on 2017-11-28.
 */

public class SettingLanguageFragment extends Fragment {

    int LanguageID = 0;
    LinearLayout Korean;
    LinearLayout English;
    LinearLayout Japanese;
    LinearLayout Chinese;
    LinearLayout.OnClickListener onClickListener;

    int LLBackgroundColor = 0x14000000;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_setting_language, container, false);
        getConstantData();

        Korean      = (LinearLayout)view.findViewById(R.id.ll_setting_language_korean);
        English     = (LinearLayout)view.findViewById(R.id.ll_setting_language_English);
        Japanese    = (LinearLayout)view.findViewById(R.id.ll_setting_language_japanese);
        Chinese     = (LinearLayout)view.findViewById(R.id.ll_setting_language_chinese);

        setOnClickListener();

        Korean.setOnClickListener(this.onClickListener);
        English.setOnClickListener(this.onClickListener);
        Japanese.setOnClickListener(this.onClickListener);
        Chinese.setOnClickListener(this.onClickListener);

        setLLBackground();

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        /*SharedPreferences pref = this.getActivity().getSharedPreferences(Constants.SETVALUE.PREF_DATA_NAME, 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putInt(PREF_DATA_LANGUAGE, LanguageID);
        edit.commit();*/
    }

    private void getConstantData(){/*
        SharedPreferences pref = this.getActivity().getSharedPreferences(PREF_DATA_NAME, 0);
        LanguageID = pref.getInt(PREF_DATA_LANGUAGE, 0);*/
    }

    private void setOnClickListener(){
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.ll_setting_language_korean:
                        LanguageID = 0;
                        break;
                    case R.id.ll_setting_language_English:
                        //LanguageID = 1;
                        break;
                    case R.id.ll_setting_language_japanese:
                        //LanguageID = 2;
                        break;
                    case R.id.ll_setting_language_chinese:
                        //LanguageID = 3;
                        break;
                }
                fragmentTransaction.remove(SettingLanguageFragment.this);
                fragmentManager.popBackStack();
            }
        };
    }

    private void setLLBackground(){
        switch (LanguageID){
            case 0 :
                Korean.setBackgroundColor(LLBackgroundColor);
                break;
            case 1 :
                English.setBackgroundColor(LLBackgroundColor);
                break;
            case 2 :
                Japanese.setBackgroundColor(LLBackgroundColor);
                break;
            case 3 :
                Chinese.setBackgroundColor(LLBackgroundColor);
                break;
        }
    }
}
