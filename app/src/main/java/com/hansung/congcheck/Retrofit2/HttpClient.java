package com.hansung.congcheck.Retrofit2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hyung on 2017-11-05.
 */

public class HttpClient {
    private static String BaseURL = "http://58.180.17.43";
    private static Retrofit getRetrofitInstance(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static HttpService getStationListService(){
        return getRetrofitInstance(BaseURL).create(HttpService.class);
    }

}
