package com.hansung.congcheck.Fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hansung.congcheck.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutPlaceFragment extends Fragment {

    String TAG = "AboutPlaceFragment";
    String[] placeInfo;

    TextView tv_opt1_placeInfo;

    public AboutPlaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view;
        view =  inflater.inflate(R.layout.fragment_about_place, container, false);
        //view = inflater.inflate(R.layout.fragment_otherplace, container,false);

        //tv_opt1_placeInfo = (TextView)view.findViewById(R.id.tv_opt1_placeInfo);

        //placeInfo = getResources().getStringArray(R.array.AboutPlace_information);

        setLanguageforinformation();

        return view;
    }

    public void setLanguageforinformation(){
        /*switch (Constants.Language.getLanguage()) {
            case R.id.rB_korean:
                //tv_opt1_placeInfo.setText(placeInfo[0]);
                break;
            case R.id.rB_english:
                //tv_opt1_placeInfo.setText(placeInfo[1]);
                break;
            case R.id.rB_japanese:
                //tv_opt1_placeInfo.setText(placeInfo[2]);
                break;
            case R.id.rB_chinese:
                //tv_opt1_placeInfo.setText(placeInfo[3]);
                break;
            default:
                //tv_opt1_placeInfo.setText(placeInfo[0]);
                break;
        }*/
    }
}
