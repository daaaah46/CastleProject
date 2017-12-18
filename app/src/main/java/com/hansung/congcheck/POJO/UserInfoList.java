package com.hansung.congcheck.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by user5 on 2017-11-21.
 */

public class UserInfoList implements Serializable
{

    @SerializedName("user_info")
    @Expose
    private List<UserInfo> userInfo = null;
    private final static long serialVersionUID = 6222311121809649330L;

    public List<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(List<UserInfo> userInfo) {
        this.userInfo = userInfo;
    }

    public class UserInfo implements Serializable
    {

        @SerializedName("num")
        @Expose
        private String num;
        @SerializedName("phoneNum")
        @Expose
        private String phoneNum;

        private final static long serialVersionUID = 3462139943968842977L;

        public UserInfo(String phoneNum){
            this.phoneNum = phoneNum;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }
    }
}