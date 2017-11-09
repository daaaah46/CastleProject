package com.hansung.congcheck.Utility;

import android.graphics.Bitmap;

/**
 * Created by user5 on 2017-10-17.
 */

public class GridviewItem {
    private Bitmap image;

    public GridviewItem(Bitmap image){
        super();
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
