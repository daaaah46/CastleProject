package com.hansung.congcheck.Retrofit2;

import com.hansung.congcheck.POJO.InsertResult;
import com.hansung.congcheck.POJO.LocationAirvalueList;
import com.hansung.congcheck.POJO.LocationInfoList;
import com.hansung.congcheck.POJO.UserInfoList;
import com.hansung.congcheck.POJO.UserVisitData;
import com.hansung.congcheck.POJO.WeatherinUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by hyung on 2017-11-05.
 */

public interface HttpService {

    @GET("/hansung/location_airvalue.php")
    Call<LocationAirvalueList> getLocation_AirValue(@Query("location_Id") String location_id, @Query("count") String count);

    @GET("/hansung/location_info.php")
    Call<LocationInfoList> getLocation_Info();

    @GET("/hansung/insert_airvalue.php")
    Call<InsertResult> insert_airValue(@Query("location_id") String location_id, @Query("finedust") String finedust,
                                       @Query("ozon") String ozon, @Query("temp") String temp, @Query("humi") String humi);

    @GET("/hansung/user_info.php")
    Call<UserInfoList> getUserInfo(@Query("phoneNum") String userNumber);

    @POST("/hansung/insert_user.php")
    Call<UserInfoList> setUserInfo(@Query("phoneNum") String userNumber);

    @GET("/hansung/s_airvalue.php")
    Call<WeatherinUser> getWeatherInfo(@Query("station_name") String sname);

    @GET("/hansung/visit_info.php")
    Call<UserVisitData> getUservisitInfo(@Query("num") String num);

    @POST("/hansung/insert_visit.php")
    Call<UserVisitData> setUservisitInfo(@Query("num") String num, @Query("location")String locationName);

    @POST("/hansung/insert_visit_num.php")
    Call<UserVisitData> createUserDB(@Query("num")String num);

}
