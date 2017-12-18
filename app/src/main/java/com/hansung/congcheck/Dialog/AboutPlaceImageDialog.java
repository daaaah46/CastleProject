package com.hansung.congcheck.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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

    public void UserImageset(Bitmap bitmap){
        PlaceImage.setImageBitmap(bitmap);
    }

    public void PlaceImageset(int placenumber){
        switch (placenumber){
            case Constants.PLACENUMBER.NAKSANROAD:
                PlaceImage.setImageResource(R.mipmap.home_place_naksanroad_image);
                break;
            case Constants.PLACENUMBER.NAKSANPARK:
                PlaceImage.setImageResource(R.mipmap.home_place_naksanpark_image);
                break;
            case Constants.PLACENUMBER.HANSUNGUNI:
                PlaceImage.setImageResource(R.mipmap.home_place_hansunguni_image);
                break;
            case Constants.PLACENUMBER.HYEHWADOOR:
                PlaceImage.setImageResource(R.mipmap.home_place_hyehwadoor_image);
                break;
        }
    }
}
