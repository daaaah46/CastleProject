package com.hansung.congcheck.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user5 on 2017-12-05.
 */

public class WeatherinUser implements Serializable {

    @SerializedName("s_airdata")
    @Expose
    private List<Weather> weatherInfo = null;

    public List<Weather> getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(List<Weather> weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    private final static long serialVersionUID = 6222311121804578330L;


    public class Weather implements Serializable
    {

        @SerializedName("khaiValue")
        @Expose
        private String kahiValue;
        @SerializedName("pm10Value")
        @Expose
        private String pm10Value;

        @SerializedName("pm25Value")
        @Expose
        private String pm25Value;

        @SerializedName("o3Value")
        @Expose
        private String o3Value;

        private final static long serialVersionUID = 3469842943968842977L;

        public String getKahiValue() {
            return kahiValue;
        }

        public void setKahiValue(String kahiValue) {
            this.kahiValue = kahiValue;
        }

        public String getPm10Value() {
            return pm10Value;
        }

        public void setPm10Value(String pm10Value) {
            this.pm10Value = pm10Value;
        }

        public String getO3Value() {
            return o3Value;
        }

        public void setO3Value(String o3Value) {
            this.o3Value = o3Value;
        }

        public String getPm25Value() {
            return pm25Value;
        }

        public void setPm25Value(String pm25Value) {
            this.pm25Value = pm25Value;
        }
    }
}
