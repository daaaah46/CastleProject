package com.hansung.congcheck.Utility;

/**
 * Created by hyung on 2017-09-25.
 */



public class Constants {
    public static final class DB{
        public static String DBName = "sql_userImage.db";
        public static int DBVersion = 1;
    }

    public static final class WEATHER{
        public static String Major = "0x0000";
        public static String Minor = "0x0000";
    }

    //장소 식별 번호
    public static final class PLACENUMBER{
        public static final int NAKSANROAD = 1;
        public static final int NAKSANPARK = 2;
        public static final int HANSUNGUNI = 3;
        public static final int HYEHWADOOR = 4;
    }

    public static class UserVisit{
        public static boolean getServerData = false;
        public static boolean Naksanroad;
        public static boolean Naksanpark;
        public static boolean Hansunguni;
        public static boolean Hyehwadoor;

        public static boolean isGetServerData() {
            return getServerData;
        }

        public static void setGetServerData(boolean getServerData) {
            UserVisit.getServerData = getServerData;
        }

        public static boolean isNaksanroad() {
            return Naksanroad;
        }

        public static void setNaksanroad(boolean naksanroad) {
            UserVisit.Naksanroad = naksanroad;
        }

        public static boolean isNaksanpark() {
            return Naksanpark;
        }

        public static void setNaksanpark(boolean naksanpark) {
            UserVisit.Naksanpark = naksanpark;
        }

        public static boolean isHansunguni() {
            return Hansunguni;
        }

        public static void setHansunguni(boolean hansunguni) {
            UserVisit.Hansunguni = hansunguni;
        }

        public static boolean isHyehwadoor() {
            return Hyehwadoor;
        }

        public static void setHyehwadoor(boolean hyehwadoor) {
            UserVisit.Hyehwadoor = hyehwadoor;
        }
    }

}
