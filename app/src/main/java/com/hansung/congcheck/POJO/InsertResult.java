package com.hansung.congcheck.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hyung on 2017-11-05.
 */

public class InsertResult implements Serializable
{

    @SerializedName("result")
    @Expose
    private String result;
    private final static long serialVersionUID = -3073949057915840717L;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}