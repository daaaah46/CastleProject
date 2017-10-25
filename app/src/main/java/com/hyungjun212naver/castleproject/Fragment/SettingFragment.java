package com.hyungjun212naver.castleproject.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.hyungjun212naver.castleproject.R;
import com.hyungjun212naver.castleproject.Utility.Constants;

public class SettingFragment extends Fragment {

    RadioGroup rG_language;
    Switch switch_popup;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_option4, container, false);

        rG_language = (RadioGroup)view.findViewById(R.id.rG_language);
        rG_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                int id = rG_language.getCheckedRadioButtonId();
                Constants.Language.setLanguage(id);
                Snackbar.make(container, "언어설정을 저장하였습니다.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        switch_popup = (Switch)view.findViewById(R.id.switch_popup);
        switch_popup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    Snackbar.make(container, "팝업 설정을 On 하였습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    Snackbar.make(container, "팝업 설정을 Off 하였습니다.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                Constants.Popup.setPopupFlag(ischecked);
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
}
