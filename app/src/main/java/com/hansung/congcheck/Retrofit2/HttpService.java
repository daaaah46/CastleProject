package com.hansung.congcheck.Retrofit2;

import com.hansung.congcheck.POJO.InsertResult;
import com.hansung.congcheck.POJO.LocationAirvalueList;
import com.hansung.congcheck.POJO.LocationInfoList;

import retrofit2.Call;
import retrofit2.http.GET;
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
}
