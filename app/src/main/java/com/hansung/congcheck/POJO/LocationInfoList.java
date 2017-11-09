package com.hansung.congcheck.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hyung on 2017-11-05.
 */

public class LocationInfoList implements Serializable
{

    @SerializedName("location_info")
    @Expose
    private List<LocationInfo> locationInfo = null;
    private final static long serialVersionUID = 1313810836688587430L;

    public List<LocationInfo> getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(List<LocationInfo> locationInfo) {
        this.locationInfo = locationInfo;
    }

    public class LocationInfo implements Serializable
    {

        @SerializedName("locatin_id")
        @Expose
        private String locatinId;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("location_latitude")
        @Expose
        private String locationLatitude;
        @SerializedName("location_longitude")
        @Expose
        private String locationLongitude;
        @SerializedName("location_addr")
        @Expose
        private String locationAddr;
        private final static long serialVersionUID = -6956512155865434857L;

        public String getLocatinId() {
            return locatinId;
        }

        public void setLocatinId(String locatinId) {
            this.locatinId = locatinId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getLocationLatitude() {
            return locationLatitude;
        }

        public void setLocationLatitude(String locationLatitude) {
            this.locationLatitude = locationLatitude;
        }

        public String getLocationLongitude() {
            return locationLongitude;
        }

        public void setLocationLongitude(String locationLongitude) {
            this.locationLongitude = locationLongitude;
        }

        public String getLocationAddr() {
            return locationAddr;
        }

        public void setLocationAddr(String locationAddr) {
            this.locationAddr = locationAddr;
        }

    }
}
