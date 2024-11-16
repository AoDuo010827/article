package com.ys.rkapi;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MySDK {
    private static MyManager myManager;

    public static void init(Context context) {
        myManager = MyManager.getInstance(context);
        myManager.bindAIDLService(context);
    }

    public static int isHDMI() {
        int state ;
        boolean status = myManager.getHdmiinStatus();
        if (status)
            state = 1;
        else
            state = 0;
        return state;
    }

    public static boolean screenshot(String path) {
        return myManager.takeScreenshot(path);
    }

    public static boolean screenshot(String path, int type) {
        switch (type){
            case 1://副屏
                return myManager.viceScreenshot(path);
            case 0://主屏
            default:
                return myManager.takeScreenshot(path);
        }
    }

    public static int getHdmiinResolution() {
        int type = 0;
        int hight = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hight = myManager.getScreenResolution(1).getMode().getPhysicalHeight();
        }
        if (hight == 1080)
            type = 1;
        else if (type == 720)
            type = 2;
        else
            type = 0;
        return type;
    }

    public static boolean uninstallApplication(String packagename) {
        return myManager.unInstallApk(packagename);
    }

    public static void timingSwitcher(long closetime,long opentime,boolean type){
        if (type) {
            String shutdownTime = longToDate(closetime);
            int offYear = Integer.valueOf(shutdownTime.substring(0, 4));
            int offMonth = Integer.valueOf(shutdownTime.substring(5, 7));
            int offDate = Integer.valueOf(shutdownTime.substring(8, 10));
            int offHour = Integer.valueOf(shutdownTime.substring(11, 13));
            int offMinute = Integer.valueOf(shutdownTime.substring(14, 16));

            String bootTime = longToDate(opentime);
            int onYear = Integer.valueOf(bootTime.substring(0, 4));
            int onMonth = Integer.valueOf(bootTime.substring(5, 7));
            int onDate = Integer.valueOf(bootTime.substring(8, 10));
            int onHour = Integer.valueOf(bootTime.substring(11, 13));
            int onMinute = Integer.valueOf(bootTime.substring(14, 16));

            int[] timeoffArray = new int[5];
            timeoffArray[0] = offYear;
            timeoffArray[1] = offMonth;
            timeoffArray[2] = offDate;
            timeoffArray[3] = offHour;
            timeoffArray[4] = offMinute;

            int[] timeonArray = new int[5];
            timeonArray[0] = onYear;
            timeonArray[1] = onMonth;
            timeonArray[2] = onDate;
            timeonArray[3] = onHour;
            timeonArray[4] = onMinute;
            myManager.setPowerOnOff(timeonArray, timeoffArray);
        }else {
            myManager.clearPowerOnOffTime();
        }
    }



    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        return sd.format(date);
    }
}

