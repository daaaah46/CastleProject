package com.hyungjun212naver.castleproject.Utility;

/**
 * Created by hyung on 2017-09-25.
 */



public class Constants {

    public static final class LOGIN {
        public static final String LOGINSUCCESS = "loginsuccess";
        public static final String LOGINFAIL = "loginfail";
        public static String LOGIN_NAME = "";
        public static String LOGIN_ID = "";
    }

    public static final class WEATHER{
        public static String Dust = "";
        public static String O3 = "";
        public static String Temperature = "";
        public static String Humidity = "";
    }

    public static class UserVisit{
        public static boolean getServerData = false;
        public static boolean place01;
        public static boolean place02;
        public static boolean place03;
        public static boolean place04;

        public static boolean isGetServerData() {
            return getServerData;
        }

        public static void setGetServerData(boolean getServerData) {
            UserVisit.getServerData = getServerData;
        }

        public static boolean isPlace01() {
            return place01;
        }

        public static void setPlace01(boolean place01) {
            UserVisit.place01 = place01;
        }

        public static boolean isPlace02() {
            return place02;
        }

        public static void setPlace02(boolean place02) {
            UserVisit.place02 = place02;
        }

        public static boolean isPlace03() {
            return place03;
        }

        public static void setPlace03(boolean place03) {
            UserVisit.place03 = place03;
        }

        public static boolean isPlace04() {
            return place04;
        }

        public static void setPlace04(boolean place04) {
            UserVisit.place04 = place04;
        }
    }
}
