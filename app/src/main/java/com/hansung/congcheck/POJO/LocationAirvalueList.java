package com.hansung.congcheck.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hyung on 2017-11-05.
 */

public class LocationAirvalueList implements Serializable
{

    @SerializedName("location_airvalue")
    @Expose
    private List<LocationAirvalue> locationAirvalue = null;
    private final static long serialVersionUID = -2658634443564688347L;

    public List<LocationAirvalue> getLocationAirvalue() {
        return locationAirvalue;
    }

    public void setLocationAirvalue(List<LocationAirvalue> locationAirvalue) {
        this.locationAirvalue = locationAirvalue;
    }


    public class LocationAirvalue implements Serializable
    {

        @SerializedName("locatin_id")
        @Expose
        private String locatinId;
        @SerializedName("mtime")
        @Expose
        private String mtime;
        @SerializedName("ozon")
        @Expose
        private String ozon;
        @SerializedName("finedust")
        @Expose
        private String finedust;
        @SerializedName("temp")
        @Expose
        private String temp;
        @SerializedName("humi")
        @Expose
        private String humi;
        private final static long serialVersionUID = -935882346766827487L;

        public String getLocatinId() {
            return locatinId;
        }

        public void setLocatinId(String locatinId) {
            this.locatinId = locatinId;
        }

        public String getMtime() {
            return mtime;
        }

        public void setMtime(String mtime) {
            this.mtime = mtime;
        }

        public String getOzon() {
            return ozon;
        }

        public void setOzon(String ozon) {
            this.ozon = ozon;
        }

        public String getFinedust() {
            return finedust;
        }

        public void setFinedust(String finedust) {
            this.finedust = finedust;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getHumi() {
            return humi;
        }

        public void setHumi(String humi) {
            this.humi = humi;
        }

    }
}