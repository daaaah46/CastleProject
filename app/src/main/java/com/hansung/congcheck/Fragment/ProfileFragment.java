package com.hansung.congcheck.Fragment;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.Constants;

public class ProfileFragment extends Fragment {

    int visitplacenum = 0;

    TextView stampnum;
    TextView isvisit;
    TextView tvnaksanroad;
    TextView tvnaksanpark;
    TextView tvhansunguni;
    TextView tvhyehwadoor;
    ImageView naksanroad;
    ImageView hansunguni;
    ImageView naksanpark;
    ImageView hyehwadoor;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        stampnum = (TextView)view.findViewById(R.id.tv_profile_stampnum);
        isvisit = (TextView)view.findViewById(R.id.tv_profile_isvisit);
        naksanroad = (ImageView)view.findViewById(R.id.iv_profile_naksanroad);
        hansunguni = (ImageView)view.findViewById(R.id.iv_profile_hansunguni);
        naksanpark = (ImageView)view.findViewById(R.id.iv_profile_naksanpark);
        hyehwadoor = (ImageView)view.findViewById(R.id.iv_profile_hyehwadoor);

        tvnaksanroad = (TextView)view.findViewById(R.id.tv_profile_naksanroad);
        tvnaksanpark = (TextView)view.findViewById(R.id.tv_profile_naksanpark);
        tvhansunguni = (TextView)view.findViewById(R.id.tv_profile_hansunguni);
        tvhyehwadoor = (TextView)view.findViewById(R.id.tv_profile_hyehwadoor);

        setInitializeUI();

        return view;
    }

    public void setInitializeUI(){
        if(Constants.UserVisit.isNaksanroad()){
            naksanroad.setImageResource(R.mipmap.stamp_visit_nansanroad);
            tvnaksanroad.setTextColor(getResources().getColor(R.color.colorBlack_80));
            visitplacenum++;
        }
        if(Constants.UserVisit.isNaksanpark()){
            naksanpark.setImageResource(R.mipmap.stamp_visit_naksanpark);
            tvnaksanpark.setTextColor(getResources().getColor(R.color.colorBlack_80));
            visitplacenum++;
        }
        if(Constants.UserVisit.isHansunguni()){
            hansunguni.setImageResource(R.mipmap.stamp_visit_hansunguni);
            tvhansunguni.setTextColor(getResources().getColor(R.color.colorBlack_80));
            visitplacenum++;
        }
        if(Constants.UserVisit.isHyehwadoor()){
            hyehwadoor.setImageResource(R.mipmap.stamp_visit_hyehwadoor);
            tvhyehwadoor.setTextColor(getResources().getColor(R.color.colorBlack_80));
            visitplacenum++;
        }
        if(visitplacenum > 0 ){
            stampnum.setText(visitplacenum+"");
            isvisit.setText("낙산공원 방문");
            isvisit.setTextColor(getResources().getColor(R.color.colorBlack_80));
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
