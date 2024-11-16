package com.ys.rkapi.product;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ys.rkapi.Constant;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.Utils;

import java.io.File;

import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 2018/11/6.
 */

public class RK3568_11 extends YS {
    public final static RK3568_11 INSTANCE = new RK3568_11();
    private static final String BACKLIGHT_IO_PATH = "/sys/bus/i2c/devices/3-0050/mainlcd_backlight";
    private static final String SECOND_BACKLIGHT_IO_PATH = "/sys/bus/i2c/devices/3-0050/mainlcd_backlight";
    @Override
    public String getRtcPath() {
        return null;
    }

    @Override
    public String getLedPath() {
        return null;
    }

    @Override
    public void takeBrightness(Context context) {

    }

    @Override
    public void setEthMacAddress(Context context, String val) {
        Toast.makeText(context, "暂不支持此功能", Toast.LENGTH_LONG).show();
    }

    @Override
    public void rotateScreen(Context context, String degree) {
        Utils.setValueToProp("persist.sys.displayrot", degree);
        File file = new File("/sys/devices/platform/ff150000.i2c/i2c-6/6-0050/rotate");
        if (file.exists()) {
            GPIOUtils.writeStringFileFor7(file, degree);
        }
        Utils.reboot(context);
    }

    @Override
    public boolean getNavBarHideState(Context context) {
        return Utils.getValueFromProp(Constant.PROP_STATUSBAR_STATE_LU).equals("0");
    }

    @Override
    public boolean isSlideShowNavBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_STATUSBAR_LU).equals("1");
    }

    @Override
    public void setSlideShowNavBar(Context context, boolean flag) {
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR_LU, "1");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_STATUSBAR_LU, "0");
    }

    @Override
    public boolean isSlideShowNotificationBarOpen() {
        return Utils.getValueFromProp(Constant.PROP_SWIPE_NOTIFIBAR_LU).equals("0");
    }

    @Override
    public void setSlideShowNotificationBar(Context context, boolean flag) {
        if (flag)
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "0");
        else
            Utils.setValueToProp(Constant.PROP_SWIPE_NOTIFIBAR_LU, "1");
    }

    @Override
    public void turnOffBackLight() {
//        try {
//            GPIOUtils.writeIntFileFor7("1",BACKLIGHT_IO_PATH);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        GPIOUtils.writeStringFileFor7(new File(BACKLIGHT_IO_PATH), "0");
    }

    @Override
    public void turnOnBackLight() {
//        try {
//            GPIOUtils.writeIntFileFor7("0",BACKLIGHT_IO_PATH);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        GPIOUtils.writeStringFileFor7(new File(BACKLIGHT_IO_PATH), "1");
    }

    @Override
    public boolean isBackLightOn() {
        return "1".equals(GPIOUtils.readGpioPG(BACKLIGHT_IO_PATH));
    }

    @Override
    public void rebootRecovery(Context context) {
        Utils.execFor7("reboot recovery");
    }

    @Override
    public boolean silentInstallApk(String apkPath) {
        return Utils.execFor7("pm install -r " + apkPath);
    }

    @Override
    public void changeScreenLight(Context context, int value) {
        Intent intent = new Intent("com.ys.set_screen_bright");
        intent.putExtra("brightValue", value);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        context.sendBroadcast(intent);
    }

    @Override
    public void turnOnHDMI() {
        // Utils.execFor7("busybox echo 0 > " + path);
        Utils.execFor7("chmod 777 /sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status");
        GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status"), "on");
    }

    @Override
    public void turnOffHDMI() {
        Utils.execFor7("chmod 777 /sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status");
        GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status"), "off");
    }

    @Override
    public void setSoftKeyboardHidden(boolean hidden) {
        if (hidden)
            Utils.setValueToProp("persist.sys.softkeyboard", "0");
        else
            Utils.setValueToProp("persist.sys.softkeyboard", "1");
    }

    @Override
    public void setDormantInterval(Context context, long time) {
        Intent intent = new Intent(Constant.DORMANT_INTERVAL);
        intent.putExtra("time_interval", time);
        intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
        context.sendBroadcast(intent);
    }

    @Override
    public int getCPUTemperature() {
        ///sys/class/thermal/thermal_zone0/temp
        String s = GPIOUtils.readGpioPGForLong("/sys/class/thermal/thermal_zone0/temp");
        int temp = Integer.parseInt(s.substring(0, 5));
        return (int) (temp / 1000);
    }

    @Override
    public void setADBOpen(boolean open) {
        if (open) {
            Utils.setValueToProp("persist.sys.usb.otg.mode", "2");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/fe8a0000.usb2-phy/otg_mode"), "peripheral");
        } else {
            Utils.setValueToProp("persist.sys.usb.otg.mode", "1");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/fe8a0000.usb2-phy/otg_mode"), "host");
        }

    }

    @Override
    public void awaken() {
        if ("1".equals(GPIOUtils.readGpioPG(BACKLIGHT_IO_PATH))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Utils.setValueToProp("persist.sys.sleep_mode", "false");
                        Utils.execFor7("chmod 777 /sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status");
                        GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/display-subsystem/drm/card0/card0-HDMI-A-1/status"), "on");
                        sleep(2000);
                        GPIOUtils.writeStringFileFor7(new File(BACKLIGHT_IO_PATH), "0");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
