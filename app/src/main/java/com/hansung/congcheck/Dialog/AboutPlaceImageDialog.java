package com.hansung.congcheck.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import com.hansung.congcheck.R;
import com.hansung.congcheck.Utility.Constants;

/**
 * Created by user5 on 2017-11-07.
 */

public class AboutPlaceImageDialog extends Dialog {

    public ImageView PlaceImage;

    public AboutPlaceImageDialog(Context context){
        super(context);
        setContentView(R.layout.dialog_aboutplace_image);
        PlaceImage  = (ImageView) findViewById(R.id.iv_dialog_placeimage);
    }

    public void PlaceImageset(int placenumber){
        switch (placenumber){
            case Constants.PLACENUMBER.NAKSANPARKPATH:
                PlaceImage.setImageResource(R.mipmap.naksan);
                break;
            case Constants.PLACENUMBER.NAKSANPARK:
                PlaceImage.setImageResource(R.mipmap.naksan);
                break;
            case Constants.PLACENUMBER.HANSUNGUNIV:
                PlaceImage.setImageResource(R.mipmap.naksan);
                break;
            case Constants.PLACENUMBER.HYEHWAMOON:
                PlaceImage.setImageResource(R.mipmap.naksan);
                break;
        }
    }
}
