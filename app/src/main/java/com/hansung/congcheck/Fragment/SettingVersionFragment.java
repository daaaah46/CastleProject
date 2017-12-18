package com.hansung.congcheck.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hansung.congcheck.R;

/**
 * Created by user5 on 2017-11-28.
 */

public class SettingVersionFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //현재는 버전 체크 코드는 안넣었음....!
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_version, container, false);
        return view;
    }
}
