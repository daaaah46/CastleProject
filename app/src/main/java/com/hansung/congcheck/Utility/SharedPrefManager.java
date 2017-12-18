package com.hansung.congcheck.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by user5 on 2017-11-28.
 */

public class SharedPrefManager {
    private static SharedPrefManager uniqueInstance;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;

    private static final String PREF_DATA_NAME = "DATA_OF_USER_CONGCHECK";
    private static final String PREF_DATA_USERNUMBER = "DATA_OF_USERNUMBER"; //string
    private static final String PREF_DATA_POP_UP = "DATA_OF_POP_UP"; //boolean
    private static final String PREF_DATA_LANGUAGE = "DATA_OF_LANGUAGE"; //int
    private static final String PREF_DATA_NAKSANROADIMAGE = "DATA_OF_NAKSANROAD";
    private static final String PREF_DATA_NAKSANPARKIMAGE = "DATA_OF_NAKSANPARK";
    private static final String PREF_DATA_HYEHWADOORIMAGE = "DATA_OF_HYEHWADOOR";
    private static final String PREF_DATA_HANSUNGUNIIMAGE = "DATA_OF_HANSUNGUNI";

    private SharedPrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_DATA_NAME, 0);
        edit = pref.edit();
    }

    public static SharedPrefManager getInstance(Context context) {
        if (uniqueInstance == null) {
            uniqueInstance = new SharedPrefManager(context);
        }
        return uniqueInstance;
    }

    public void setPrefDataUsernumber(String usernumber) {
        edit.putString(PREF_DATA_USERNUMBER, usernumber);
        edit.commit();
    }

    public String getPrefDataUsernumber() {
        return pref.getString(PREF_DATA_USERNUMBER, null);
    }

    public void setPrefDataPopup(boolean bValue) {
        edit.putBoolean(PREF_DATA_POP_UP, bValue);
        edit.commit();
    }

    public boolean getPrefDataPopup() {
        return pref.getBoolean(PREF_DATA_POP_UP, false);
    }

    public void setPrefDataLanguage(int languagenumber) {
        edit.putInt(PREF_DATA_LANGUAGE, languagenumber);
        edit.commit();
    }

    public int getPrefDataLanguage() {
        return pref.getInt(PREF_DATA_LANGUAGE, 0);
    }

    public void setPrefDataNaksanroadimage(String naksanroadimageurl) {
        ArrayList<String> urls = getStringArrayPref(PREF_DATA_NAKSANROADIMAGE);
        urls.add(naksanroadimageurl);
        edit.putString(PREF_DATA_NAKSANROADIMAGE, setStringArrayPref(urls));
        edit.commit();
    }

    public ArrayList<String> getPrefDataNaksanroadimage() {
        return getStringArrayPref(PREF_DATA_NAKSANROADIMAGE);
    }

    public void setPrefDataNaksanparkimage(String naksanparkimageurl) {
        ArrayList<String> urls = getStringArrayPref(PREF_DATA_NAKSANPARKIMAGE);
        urls.add(naksanparkimageurl);
        edit.putString(PREF_DATA_NAKSANPARKIMAGE, setStringArrayPref(urls));
        edit.commit();
    }

    public ArrayList<String> getPrefDataNaksanparkimage() {
        return getStringArrayPref(PREF_DATA_NAKSANPARKIMAGE);
    }

    public void setPrefDataHyehwadoorimage(String hyehwadoorimageurl) {

        ArrayList<String> urls = getStringArrayPref(PREF_DATA_HYEHWADOORIMAGE);
        urls.add(hyehwadoorimageurl);

        edit.putString(PREF_DATA_HYEHWADOORIMAGE, setStringArrayPref(urls));
        edit.commit();
    }

    public ArrayList<String> getPrefDataHyehwadoorimage() {
        return getStringArrayPref(PREF_DATA_HYEHWADOORIMAGE);
    }

    public void setPrefDataHansunguniimage(String hansunguniimageurl) {

        ArrayList<String> urls = getStringArrayPref(PREF_DATA_HANSUNGUNIIMAGE);
        urls.add(hansunguniimageurl);

        edit.putString(PREF_DATA_HANSUNGUNIIMAGE, setStringArrayPref(urls));
        edit.commit();
    }

    public ArrayList<String> getPrefDataHansunguniimage() {
        return getStringArrayPref(PREF_DATA_HANSUNGUNIIMAGE);
    }

    private String setStringArrayPref(ArrayList<String> values) {
        JSONArray array = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            array.put(values.get(i));
        }
        return array.toString();
    }

    private ArrayList<String> getStringArrayPref(String key) {
        ArrayList<String> urls = new ArrayList<>();
        String json = pref.getString(key, null);
        //Log.e("TAG", json);
        if (json != null) {
            try {
                JSONArray array = new JSONArray(json);
                for (int i = 0; i < array.length(); i++) {
                    String url = array.getString(i);
                    File file = new File(url);
                    if (file.exists()) { //파일 있는지 한 번 더 체크 후..!
                        urls.add(url);
                    } else continue;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}