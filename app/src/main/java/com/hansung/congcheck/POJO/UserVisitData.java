package com.hansung.congcheck.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user5 on 2017-12-05.
 */

public class UserVisitData implements Serializable {
    @SerializedName("visit")
    @Expose
    private List<VisitValue> uservisitInfo= null;
    private final static long serialVersionUID = 6222311121809649330L;

    public List<VisitValue> getUservisitInfo() {
        return uservisitInfo;
    }

    public void setUservisitInfo(List<VisitValue> uservisitInfo) {
        this.uservisitInfo = uservisitInfo;
    }

    public class VisitValue implements Serializable
    {

        @SerializedName("num")
        @Expose
        private String num;
        @SerializedName("naksanroad")
        @Expose
        private String naksanroad;

        @SerializedName("naksanpark")
        @Expose
        private String naksanpark;

        @SerializedName("hansunguni")
        @Expose
        private String hansunguni;

        @SerializedName("heyhwadoor")
        @Expose
        private String heyhwadoor;

        private final static long serialVersionUID = 3462139943895242977L;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getNaksanroad() {
            return naksanroad;
        }

        public void setNaksanroad(String naksanroad) {
            this.naksanroad = naksanroad;
        }

        public String getNaksanpark() {
            return naksanpark;
        }

        public void setNaksanpark(String naksanpark) {
            this.naksanpark = naksanpark;
        }

        public String getHansunguni() {
            return hansunguni;
        }

        public void setHansunguni(String hansunguni) {
            this.hansunguni = hansunguni;
        }

        public String getHeyhwadoor() {
            return heyhwadoor;
        }

        public void setHeyhwadoor(String heyhwadoor) {
            this.heyhwadoor = heyhwadoor;
        }
    }
}
