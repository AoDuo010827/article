package com.ys.rkapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.ys.myapi.IgetMessage;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.Utils.LogUtils;
import com.ys.rkapi.Utils.NetUtils;
import com.ys.rkapi.Utils.ShellUtils;
import com.ys.rkapi.Utils.StorageUtils;
import com.ys.rkapi.Utils.TimeUtils;
import com.ys.rkapi.Utils.VersionUtils;
import com.ys.rkapi.Utils.Utils;
import com.ys.rkapi.product.YsFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ys.rkapi.Utils.Utils.isRoot;
import static java.lang.Thread.sleep;

/**
 * Created by RYX on 2017/8/30.
 */

public class MyManager {
    private static final String TAG = "MyManager";

    private static MyManager myManager;
    public static final String BUILD_TIMESTAMP = "2019-08-27";
    private Context mContext;
    private boolean isNewApi = true;

    private DisplayManager mDisplayManager;
    private ServiceConnectedInterface serviceConnectedInterface;
    private NewApi mNewApi = new NewApi();
    private Gpio mGpio = new Gpio();
    private int version;

    private MyManager(Context context) {
        mContext = context;
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo("com.ys.ys_receiver", PackageManager.GET_META_DATA);
            mContext.getPackageManager().getApplicationLabel(appInfo);
            Constant.YSRECEIVER_PACKAGE_NAME = "com.ys.ys_receiver";
        } catch (PackageManager.NameNotFoundException e) {
            Constant.YSRECEIVER_PACKAGE_NAME = "com.yishengkj.achieve";
        }
    }

    public static synchronized MyManager getInstance(Context context) {
        if (myManager == null) {
            myManager = new MyManager(context);
        }
        return myManager;
    }

    private void sendMyBroadcast(String action) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                    intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
                }
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWithExtra(String action, String key, String value) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key, value);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWith2Extras(String action, String key1, String value1, String key2, String value2) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra(key1, value1);
            intent.putExtra(key2, value2);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    private void sendMyBroadcastWithLongExtra(String action, String key, Object value) {
        if (mContext != null) {
            String type = value.getClass().getSimpleName();
            Intent intent = new Intent();
            intent.setAction(action);
            if ("String".equals(type))
                intent.putExtra(key, (String) value);
            else if ("Integer".equals(type))
                intent.putExtra(key, (Integer) value);
            else if ("Boolean".equals(type))
                intent.putExtra(key, (Boolean) value);
            else if ("Float".equals(type))
                intent.putExtra(key, (Float) value);
            else if ("Long".equals(type))
                intent.putExtra(key, (Long) value);
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    private void setNewAPI(boolean flag) {
        isNewApi = flag;
    }


    /**
     * @return 当前API的版本信息
     * @method getApiVersion()
     * @description 获取目前API平台-版本-日期信息，如果API发生修改就需要修改此处
     * @date 20180602
     * @author sky
     */
    public String getApiVersion() {///ok
        return BuildConfig.BUILD_TIMESTAMP;
    }

    /**
     * @return 设备型号
     * @method getAndroidModle()
     * @description 获取目前设备的型号，例如rk3288、rk3399
     * @date 20180602
     * @author sky
     */
    public String getAndroidModle() {
        String module = Build.MODEL;
        if (module.contains("312x"))
            module = "rk3128";
        return module;
    }

    //

    /**
     * @return android系统的版本
     * @method getAndroidVersion()
     * @description 获取目前设备的android系统的版本，例如7.1就返回25
     * @date 20180602
     * @author sky
     */
    public String getAndroidVersion() {
        if (isNewApi && version >= 6)
            return mNewApi.getAndroidVersion();
        else
            return Build.VERSION.SDK;
    }

    /**
     * @return 内存大小
     * @method getRunningMemory()
     * @description 获取设备的硬件内存大小容量
     * @date 20180602
     * @author sky
     */
    public String getRunningMemory() {//ok
        if (isNewApi && version >= 6)
            return mNewApi.getRunningMemory();
        else
            return StorageUtils.getRealMeoSize();
    }

    //获取设备的硬件内部存储大小容量

    /**
     * @return 设备内部存储大小
     * @method getInternalStorageMemory()
     * @description 获取设备的存储大小容量
     * @date 20180602
     * @author sky
     */
    public String getInternalStorageMemory() {//ok
        if (isNewApi && version >= 6)
            return mNewApi.getInternalStorageMemory();
        else
        return StorageUtils.getRealSizeOfNand();
    }

    /**
     * @return 设备SDK版本
     * @method getFirmwareVersion()
     * @description 获取设备的固件SDK版本。
     * @date 20180602
     * @author sky
     */
    public String getFirmwareVersion() {
        if (isNewApi)
            return version + "";
        else
            return "1";
    }

    //获取设备的固件内核版本。

    /**
     * @return 内核版本
     * @method getKernelVersion()
     * @description 获取设备的固件内核版本
     * @date 20180602
     * @author sky
     */
    public String getKernelVersion() { // ok
        if (isNewApi && version >= 6)
            return mNewApi.getKernelVersion();
        else
        return VersionUtils.getKernelVersion();
    }

    //获取设备的固件系统版本和编译日期。

    /**
     * @return 设备的固件版本号
     * @method getAndroidDisplay()
     * @description 获取固件版本号
     * @date 20180602
     * @author sky
     */
    public String getAndroidDisplay() {  //ok
        if (isNewApi && version >= 6)
            return mNewApi.getAndroidDisplay();
        else
        return VersionUtils.getSystemVersionInfo();
    }

    //获取设备CPU型号

    /**
     * @return 设备CPU型号
     * @method getCPUType()
     * @description 获取设备CPU型号
     * @date 20190118
     * @author sky
     */
    public String getCPUType() {
        if (isNewApi && version >= 6)
            return mNewApi.getCPUType();
        else {
            String CPUABI = null;
            if (CPUABI == null) {
                try {
                    CPUABI = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").getInputStream())).readLine();
                } catch (Exception e) {
                    CPUABI = "armeabi";
                }
            }
            return CPUABI;
        }
//        return VersionUtils.getCpuInfo()[0];
    }

    //获取固件编译的时间p

    /**
     * @return 固件编译的时间，比如20180602
     * @method getFirmwareDate()
     * @description 固件的编译时间
     * @date 20180602
     * @author sky
     */
    public String getFirmwareDate() {  // ok
        if (isNewApi && version >= 6)
            return mNewApi.getFirmwareDate();
        else
        return MySDK.longToDate(Build.TIME);
    }


    //关机

    /**
     * @method shutdown()
     * @description 执行关机操作，走安卓标准关机流程
     * @date 20180602
     * @author sky
     */
    public void shutdown() {  // ok
        if (isNewApi && version > 3) {
            mNewApi.shutdown();
        }else {
            sendMyBroadcast(Constant.SHUTDOWN_ACTION);
        }
    }

    //重启

    /**
     * @method reboot()
     * @description 执行重启操作，走安卓标准重启流程
     * @date 20180602
     * @author sky
     */
    public void reboot() {  // ok
        if (isNewApi && version > 3) {
            mNewApi.reboot();
        }else {
            sendMyBroadcast(Constant.REBOOT_ACTION);
        }
    }


    // 截屏
    /**
     * @param path，保存图片的路径。例如/mnt/internal_sd/001.jpg
     * @return 是否截图成功，true成功，false失败
     * @method takeScreenshot
     * @description 截屏，执行此方法可将系统当前显示画面截图保存在指定路径
     * @date 20180602
     * @author sky
     */
    public boolean takeScreenshot(final String path) { // ok
        boolean isture = false;
        if (igetMessage != null) {
            try {
                isture = igetMessage.isSuccessScreenshot(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return isture;
    }


    /**
     * @param path，保存图片的路径。例如/mnt/internal_sd/001.jpg
     * @return 是否截图成功，true成功，false失败
     * @method takeScreenshot
     * @description 副屏截图，执行此方法可将系统当前显示画面截图保存在指定路径
     * @date 20180602
     * @author sky
     */
    public boolean viceScreenshot(String path) { // ok
        boolean isture = false;
        if (igetMessage != null) {
            try {
                isture = igetMessage.isSuccessViceScreenshot(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return isture;
    }

    /**
     * @param context，上下文对象。degree，旋转的角度（0/90/180/270）
     * @method rotateScreen(Context context, String degree)
     * @description 旋转屏幕方向，0度、90度、180度和270度
     * @date 20180602
     * @author sky
     */
    public boolean rotateScreen(Context context, String degree) {
        if (isNewApi && version > 3)
            return mNewApi.rotateScreen(degree);
        else
            YsFactory.getRK().rotateScreen(context, degree);
        return true;
    }

    /**
     * @return 导航栏隐藏返回true，显示则返回false
     * @method getNavBarHideState()
     * @description 获取导航栏的状态
     * @date 20180602
     * @author sky
     */
    public boolean getNavBarHideState() {
        if (isNewApi && version > 3)
            return mNewApi.getNavBarHideState();
        else
            return YsFactory.getRK().getNavBarHideState(mContext);
    }

    /**
     * @param hide，隐藏导航栏传入true，显示传入false
     * @method hideNavBar(boolean hide)
     * @description 设置显示或隐藏导航
     * @date 20180602
     * @author sky
     */
    public boolean hideNavBar(boolean hide) {
        if (isNewApi && version > 3)
            return mNewApi.hideNavBar(hide);
        else {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT > 19) {
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            }
            if (!hide) {
                intent.setAction("android.action.adtv.showNavigationBar");
                mContext.sendBroadcast(intent);
            } else {
                intent.setAction("android.action.adtv.hideNavigationBar");
                mContext.sendBroadcast(intent);
            }
        }
        return true;
    }

    /**
     * @return 滑出导航栏打开返回true，关闭返回false
     * @method isSlideShowNavBarOpen()
     * @description 查询滑出导航栏选项是否打开
     * @date 20180602
     * @author sky
     */
    public boolean isSlideShowNavBarOpen() {
        if (isNewApi && version > 3)
            return mNewApi.isSlideShowNavBarOpen();
        else
            return YsFactory.getRK().isSlideShowNavBarOpen();
    }

    /**
     * @param flag，打开滑出导航栏传入true，关闭传入false
     * @method setSlideShowNavBar(boolean flag)
     * @description 打开或关闭滑出导航栏
     * @date 20180602
     * @author sky
     */
    public boolean setSlideShowNavBar(boolean flag) {
        if (isNewApi && version > 3)
            return mNewApi.setSlideShowNavBar(flag);
        else
            YsFactory.getRK().setSlideShowNavBar(mContext, flag);
        return true;
    }

    /**
     * @return 下拉通知栏打开返回true，否则返回false
     * @method isSlideShowNotificationBarOpen()
     * @description 查询下拉通知栏是否打开
     * @date 20180602
     * @author sky
     */
    public boolean isSlideShowNotificationBarOpen() {
        if (isNewApi && version > 3)
            return mNewApi.isSlideShowNotificationBarOpen();
        else
            return YsFactory.getRK().isSlideShowNotificationBarOpen();
    }

    /**
     * @param flag，打开下拉通知栏传入true，禁止下拉通知栏传入false
     * @method setSlideShowNotificationBar(boolean flag)
     * @description 设置 打开或关闭下拉通知栏
     * @date 20180602
     * @author sky
     */
    public boolean setSlideShowNotificationBar(boolean flag) {
        if (isNewApi && version > 3)
            return mNewApi.setSlideShowNotificationBar(flag);
        else
            YsFactory.getRK().setSlideShowNotificationBar(mContext, flag);
        return true;
    }

    /**
     * @param context，上下文对象
     * @return 屏幕宽度
     * @method getDisplayWidth(Context context)
     * @description 获取屏幕宽
     * @date 20180602
     * @author sky
     */
    public int getDisplayWidth(Context context) {
        if (isNewApi && version > 3)
            return mNewApi.getDisplayHeight_width()[0];
        else {
            String path = GPIOUtils.readGpioPGForLong("/sys/class/display/mode");
            if (path.contains("2160p60hz")) {
                return 3840;
            }
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(dm);
            int width = dm.widthPixels;         // 屏幕宽度（像素）
            int height = dm.heightPixels;       // 屏幕高度（像素）
            float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
            // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
            int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
            return screenWidth;
        }
    }

    /**
     * @param context，上下文对象
     * @return 屏幕高度
     * @method getDisplayHeight(Context context)
     * @description 获取屏幕高
     * @date 20180602
     * @author sky
     */
    public int getDisplayHeight(Context context) {
        if (isNewApi && version > 3)
            return mNewApi.getDisplayHeight_width()[1];
        else {
            String path = GPIOUtils.readGpioPGForLong("/sys/class/display/mode");
            if (path.contains("2160p60hz")) {
                return 2160;
            }
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getRealMetrics(dm);
            int width = dm.widthPixels;         // 屏幕宽度（像素）
            int height = dm.heightPixels;       // 屏幕高度（像素）
            float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
            // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
            int screenHeight = (int) (height / density);// 屏幕高度(dp)
            return screenHeight;
        }
    }

    public int[] getDisplayHeight_width(Activity activity) {
        if (isNewApi && version > 3)
            return mNewApi.getDisplayHeight_width();
        else {
            int[] display = new int[2];
            String path = GPIOUtils.readGpioPGForLong("/sys/class/display/mode");
            if (path.contains("2160p60hz")) {
                display[0] = 3840;
                display[1] = 2160;
                return display;
            }
            Point point = new Point();
            activity.getWindowManager().getDefaultDisplay().getRealSize(point);
            display[0] = point.x;
            display[1] = point.y;
            return display;
        }
    }


    /**
     * @method turnOffBackLight()
     * @description 关闭屏幕背光，仅仅是关闭背光，其他系统功能不影响
     * @date 20180602
     * @author sky
     */
    public boolean turnOffBackLight() {
        if (isNewApi && version > 3)
            return mNewApi.turnOffBackLight();
        else
            YsFactory.getRK().turnOffBackLight();
        return true;
    }

    /**
     * @method turnOnBackLight()
     * @description 打开屏幕背光，跟turnOffBackLight()方法相对应
     * @date 20180602
     * @author sky
     */
    public boolean turnOnBackLight() {
        if (isNewApi && version > 3)
            return mNewApi.turnOnBackLight();
        else
            YsFactory.getRK().turnOnBackLight();
        return true;
    }

    public boolean changeSecondBackLight(int value) {
        if (isNewApi && version > 3)
            return mNewApi.changeSecondaryScreenLight(value);
        else
            Toast.makeText(mContext, "此sdk版本不支持此功能", Toast.LENGTH_SHORT).show();
        return false;

    }

    public int getSecondBackLight() {
        if (isNewApi && version > 3)
            return mNewApi.getSecondaryScreenBrightness();
        else
            Toast.makeText(mContext, "此sdk版本不支持此功能", Toast.LENGTH_SHORT).show();
        return 0;
    }

    /**
     * @return 当前背光是开返回true，否则返回false
     * @method isBacklightOn()
     * @description 获取背光是否打开
     * @date 20181122
     * @author sky
     */
    public boolean isBacklightOn() {
        if (isNewApi && version > 3)
            return mNewApi.isBacklightOn();
        else
            return YsFactory.getRK().isBackLightOn();
    }

    /**
     * @return int，返回的亮度范围是0-100
     * @method getSystemBrightness()
     * @description 获取当前设备背光亮度
     * @date 20181122
     * @author sky
     */
    public int getSystemBrightness() {
        if (isNewApi && version > 3)
            return mNewApi.getSystemBrightness();
        else {
            int systemBrightness;
            int value = 0;
            try {
                systemBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                value = systemBrightness * 100 / 255;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return value;
        }
    }


    /**
     * @method turnOnHDMI()
     * @description 打开HDMI输出，与turnOffHDMI()相对应
     * @date 20180602
     * @author sky
     */
    public boolean turnOnHDMI() {
        if (isNewApi && version > 3)
            return mNewApi.turnOnHDMI();
        else
            YsFactory.getRK().turnOnHDMI();
        return true;
    }

    /**
     * @method turnOffHDMI()
     * @description 关闭HDMI输出，仅仅关闭信号输出，其他系统功能不影响
     * @date 20180602
     * @author sky
     */
    public boolean turnOffHDMI() {
        if (isNewApi && version > 3)
            return mNewApi.turnOffHDMI();
        else
            YsFactory.getRK().turnOffHDMI();
        return true;
    }

    //升级固件
    // 固件存放的绝对路径

    /**
     * @param absolutePath，待升级固件放置的绝对路径
     * @method upgradeSystem(String absolutePath)
     * @description 升级固件
     * @date 20180602
     * @author sky
     */
    public boolean upgradeSystem(String absolutePath) {
        if (isNewApi && version > 3)
            return mNewApi.upgradeSystem(absolutePath);
        else {
            if (Build.VERSION.SDK.equals("27") && getAndroidModle().equals("px30_e") || Build.VERSION.SDK_INT > 27) {
                Intent intent = new Intent();
                intent.setAction(Constant.FIRMWARE_UPGRADE_ACTION);
                intent.putExtra(Constant.FIRMWARE_UPGRADE_KEY, absolutePath);
                intent.setComponent(new ComponentName("android.rockchip.update.service", "android.rockchip.update.service.RKUpdateReceiver"));//第一个参数包名，第二个参数广播类
                mContext.sendBroadcast(intent);
            } else if (Build.VERSION.SDK.equals("27") && getAndroidModle().equals("msm895")) {
                Intent intent = new Intent();
                intent.setAction(Constant.FIRMWARE_UPGRADE_ACTION);
                intent.putExtra(Constant.FIRMWARE_UPGRADE_KEY, absolutePath);
                intent.setPackage("com.ys.gtupdatezip");
                mContext.sendBroadcast(intent);
            } else {
                sendMyBroadcastWithExtra(Constant.FIRMWARE_UPGRADE_ACTION, Constant.FIRMWARE_UPGRADE_KEY, absolutePath);
            }
            Utils.setValueToProp("persist.sys.ota.path", absolutePath);
        }
        return true;
    }

    /**
     * @param flag，true，弹窗提示，false，不弹窗直接升级
     * @method setUpdateSystemWithDialog(boolean flag)
     * @description 设置升级固件时是否弹窗提示
     * @date 20200327
     * @author sky
     */
    public boolean setUpdateSystemWithDialog(boolean flag) {
        if (isNewApi && version > 3)
            return mNewApi.setUpdateSystemWithDialog(flag);
        else {
            Utils.setValueToProp("persist.sys.ota.customdefine", "true");
            if (flag)
                Utils.setValueToProp("persist.sys.ota.noclick", "0");
            else
                Utils.setValueToProp("persist.sys.ota.noclick", "1");
        }
        return true;
    }

    /**
     * @param flag，true，删除，false，不删除
     * @method setUpdateSystemDelete(boolean flag)
     * @description 设置升级固件时后，固件包是否从文件中删除
     * @date 20200327
     * @author sky
     */
    public boolean setUpdateSystemDelete(boolean flag) {
        if (isNewApi && version > 3)
            return mNewApi.setUpdateSystemDelete(flag);
        else {
            Utils.setValueToProp("persist.sys.ota.customdefine", "true");
            if (flag)
                Utils.setValueToProp("persist.sys.ota.delete", "1");
            else
                Utils.setValueToProp("persist.sys.ota.delete", "0");
        }
        return true;
    }

    //重启进入Recovery模式

    /**
     * @method rebootRecovery()
     * @description 恢复出厂设置
     * @date 20180602
     * @author sky
     */
    public boolean rebootRecovery() { // ok
        if (isNewApi && version > 3)
            return mNewApi.rebootRecovery();
        else {
            if (Build.VERSION.SDK_INT >= 25) {
                Intent intent = new Intent("com.ys.recovery_system");
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
                mContext.sendBroadcast(intent);
            } else
                YsFactory.getRK().rebootRecovery(mContext);
        }
        return true;
    }

    //静默安装APK
    //安装成功返回true 否则返回false

    /**
     * @param apkPath，需要安装的apk的绝对路径
     * @return 静默安装成功返回true，否则返回false
     * @method silentInstallApk(String apkPath)
     * @description 静默安装apk，采用的方法是pm install -r
     * @date 20180602
     * @author sky
     */
    public boolean silentInstallApk(String apkPath, boolean isStartApk) {//////  ok
        if (isNewApi && version > 3)
            return mNewApi.silentInstallApk(apkPath, isStartApk);
        else {
            if (Build.VERSION.SDK_INT <= 19)
                return YsFactory.getRK().silentInstallApk(apkPath);
            else {
                Intent intent = new Intent("com.ys.silent_install");
                intent.putExtra("isStartApk", isStartApk);
                intent.putExtra("path", apkPath);
                if (Build.VERSION.SDK_INT >= 27) {
                    intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
                }
                mContext.sendBroadcast(intent);
                return true;
            }
        }
    }

    //设置以太网Mac地址
    public void setEthMacAddress(String val) {
        YsFactory.getRK().setEthMacAddress(mContext, val);
    }

    //获取以太网MAC地址

    /**
     * @return 返回以太网mac地址，例如30:1F:9A:61:BA:8F
     * @method getEthMacAddress()
     * @description 获取以太网mac地址
     * @date 20180602
     * @author sky
     */
    public String getEthMacAddress() {////////  ok
        if (isNewApi && version >= 6)
            return mNewApi.getEthMacAddress();
        else
        return NetUtils.getEthMAC();
    }

    //获取以太网静态IP地址
    public void bindAIDLService(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.GET_ETH_STATIC_IP");
        intent.setComponent(new ComponentName("com.ys.ys_receiver", "com.ys.ys_receiver.AIDLService"));
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unBindAIDLService(Context context) {
        context.unbindService(serviceConnection);
    }


    private IgetMessage igetMessage;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            igetMessage = IgetMessage.Stub.asInterface(iBinder);
            try {
                mNewApi.setNewApi(igetMessage.getApi());
                mGpio.setGpio(igetMessage.getGpio());
                version = mNewApi.getFirmwareVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (serviceConnectedInterface != null)
                serviceConnectedInterface.onConnect();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            igetMessage = null;
        }
    };

    //回调接口
    public interface ServiceConnectedInterface {
        void onConnect();
    }


    //定义回调方法
    public void setConnectClickInterface(ServiceConnectedInterface serviceConnectedInterface) {
        this.serviceConnectedInterface = serviceConnectedInterface;
    }

    /**
     * @return 静态模式返回StaticIp，动态模式返回DHCP
     * @method getEthMode()
     * @description 获取以太网的模式，静态或动态
     * @date 20180602
     * @author sky
     */
    public String getEthMode() {
        Log.d(TAG, "获取以太网模式");
        String ethMode = "";
        if (igetMessage != null) {
            try {
                ethMode = igetMessage.getEthMode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return ethMode;
    }

    /**
     * @return 以太网开关打开返回true，开关关闭返回false
     * @method getEthStatus()
     * @description 获取以太网的开关状态
     * @date 20180806
     * @author sky
     */
    public boolean getEthStatus() {
        boolean flag = false;
        if (igetMessage != null) {
            try {
                flag = igetMessage.getEthStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * @return 返回true说明开启了自动确定网络时间，返回false说明关闭了此开关
     * @method isAutoSyncTime()
     * @description 获取自动确定网络时间的开关状态
     * @date 20180806
     * @author sky
     */
    public boolean isAutoSyncTime() {
        boolean flag = false;
        if (igetMessage != null) {
            try {
                flag = igetMessage.isAutoSyncTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * @return 返回当前设备以太网的网关，例如192.168.1.1
     * @method getGateway()
     * @description 获取以太网的网关
     * @date 20180806
     * @author sky
     */
    public String getGateway() {
        String gateway = "";
        if (igetMessage != null) {
            try {
                gateway = igetMessage.getGateway();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return gateway;
    }

    /**
     * @return 返回当前设备以太网的子网掩码，例如255.255.255.0
     * @method getNetMask()
     * @description 获取以太网的子网掩码
     * @date 20180806
     * @author sky
     */
    public String getNetMask() {
        String mask = "";
        if (igetMessage != null) {
            try {
                mask = igetMessage.getNetMask();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return mask;
    }

    /**
     * @return 返回当前设备以太网的DNS1，例如192.168.1.1
     * @method getEthDns1()
     * @description 获取以太网的DNS1
     * @date 20180806
     * @author sky
     */
    public String getEthDns1() {
        String dns1 = "";
        if (igetMessage != null) {
            try {
                dns1 = igetMessage.getEthDns1();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return dns1;
    }

    /**
     * @return 返回当前设备以太网的DNS2，例如192.168.1.1
     * @method getEthDns2()
     * @description 获取以太网的DNS2
     * @date 20180806
     * @author sky
     */
    public String getEthDns2() {
        String dns2 = "";
        if (igetMessage != null) {
            try {
                dns2 = igetMessage.getEthDns2();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return dns2;
    }

    //设置静态IP

    /**
     * @param IPaddr，设置的IP地址。gateWay，设置的网关。mask，设置的子网掩码。dns1和dns2是设置的dns值
     * @method setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2)
     * @description 设置以太网的模式为静态，并指定相关参数
     * @date 20180602
     * @author sky
     */
    public boolean setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2) {//ok
        if (isNewApi && version > 3)
            return mNewApi.setStaticEthIPAddress(IPaddr, gateWay, mask, dns1, dns2);
        else {
            Log.d(TAG, "setEthIPAddress 修改以太网IP");
            NetUtils.setStaticIP(mContext, IPaddr, gateWay, mask, dns1, dns2);
        }
        return true;
    }

    //获取以太网静态IP

    /**
     * @return 返回以太网的静态ip地址
     * @method getStaticEthIPAddress()
     * @description 获取以太网的静态IP
     * @date 20180602
     * @author sky
     */
    public String getStaticEthIPAddress() {//ok
        Log.d(TAG, "获取静态IP");
        String staticEthIP = "";
        if (igetMessage != null) {
            try {
                staticEthIP = igetMessage.getStaticIP();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return staticEthIP;
    }

    public boolean setTimeAidl(long time) {
        boolean istrue = false;
        if (igetMessage != null) {
            try {
                istrue = igetMessage.setCurrentTimeMillis(time);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return istrue;
    }


    //设置动态获取IP

    /**
     * @param context，上下文对象
     * @method setDhcpIpAddress(Context context)
     * @description 设置以太网的模式为动态
     * @date 20180602
     * @author sky
     */
    public boolean setDhcpIpAddress(Context context) {
        if (isNewApi && version > 3)
            return mNewApi.setDhcpIpAddress();
        else {
            Intent intent = new Intent("com.ys.set_dhcp");
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            intent.putExtra("useStaticIP", 0);
            context.sendBroadcast(intent);
        }
        return true;
    }

    public void setPppoeDial(Context context, String userName, String password) {
        Intent intent = new Intent("com.ys.set_pppoe_dial");
        intent.putExtra("userName", userName);
        intent.putExtra("password", password);
        context.sendBroadcast(intent);
    }

    //获取以太网动态IP地址

    /**
     * @return 返回以太网动态ip地址
     * @method getDhcpIpAddress()
     * @description 获取以太网的动态IP地址
     * @date 20180602
     * @author sky
     */
    public String getDhcpIpAddress() {
        String address = "";
        if (Build.VERSION.SDK_INT >= 27) {
            if (igetMessage != null) {
                try {
                    address = igetMessage.getDhcpIpAddress();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else
            address = NetUtils.getDynamicEthIPAddress(mContext);
        return address;
    }

    //以太网使能 OnOff true 连接 false断开

    /**
     * @param enable，打开以太网开关传入true，关闭以太网开关传入false
     * @method ethEnabled(boolean enable)
     * @description 控制以太网开关
     * @date 20180602
     * @author sky
     */
    public boolean ethEnabled(boolean enable) {
        if (isNewApi && version > 3)
            return mNewApi.ethEnabled(enable);
        else {
            if (Build.VERSION.SDK_INT >= 27) {
                Intent intent = new Intent(Constant.SET_ETH_ENABLE_ACTION);
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
                intent.putExtra(Constant.ETH_MODE, enable);
                mContext.sendBroadcast(intent);
            } else
                NetUtils.setEthernetEnabled(mContext, enable);
        }
        return true;
    }

    /**
     * @param hidden，传入true，说明隐藏软键盘，传入false就是显示软键盘
     * @method setSoftKeyboardHidden(boolean hidden)
     * @description 控制软键盘是否弹出（主要是EditText控件）
     * @date 20181126
     * @author sky
     */
    public boolean setSoftKeyboardHidden(boolean hidden) {
        if (isNewApi && version > 3)
            return mNewApi.setSoftKeyboardHidden(hidden);
        else
            YsFactory.getRK().setSoftKeyboardHidden(hidden);
        return true;
    }

    // 获取外置SD卡的路径

    /**
     * @return 返回外置SD卡路径
     * @method getSDcardPath()
     * @description 获取外置SD卡路径
     * @date 20180602
     * @author sky
     */
    public String getSDcardPath() {/////  ok
        if (isNewApi && version >= 6)
            return mNewApi.getSDcardPath();
        else
        return StorageUtils.getSDcardDir(mContext);
    }

    // 获取外置U盘的路径
    // mum 表示第几个U盘，从0开始。
    // 注意要插入U盘后才能有U盘路径！！！！！

    /**
     * @param num，从0开始，表示第几个U盘
     * @return u盘的路径
     * @method getUSBStoragePath(int num)
     * @description 获取外置U盘路径
     * @date 20180602
     * @author sky
     */
    public String getUSBStoragePath(int num) {  // ok
        if (isNewApi && version >= 6)
            return mNewApi.getUSBStoragePath(num);
        else {
            List<String> allPath = getAllUsbStoragePath();
            if(num >= allPath.size() || num < 0){
                return "";
            }else{
                return allPath.get(num);
            }
        }
    }

    /**
     * 获取所有usb存储失败的路径集合
     * @return 所有的usb存储路径
     */
    public List<String> getAllUsbStoragePath(){
        return StorageUtils.getUsbDrivePath(mContext);
    }

    // 卸载外部存储，如果外部存储存在的话。
    // path 表示存储路径。
    // !!!!!!!!!!!!注意path 不能错，类似于"/mnt/usb_storage/USB_DISK3"
    public boolean unmountVolume(String path) {  // ok
        if (isNewApi && version > 3)
            return mNewApi.unmountVolume(path);
        else
            sendMyBroadcastWith2Extras(Constant.MOUNT_USB_ACTION, Constant.MOUNT_ENABLE_KEY, "0", Constant.MOUNT_POINT_KEY, path);
        return true;
    }

    // 挂载外部存储，如果外部存储存在的话。
    // path 表示存储路径
    // !!!!!!!!!!!!注意path 不能错，类似于"/mnt/usb_storage/USB_DISK3"
    public boolean mountVolume(String path) {  //ok
        if (isNewApi && version > 3)
            return mNewApi.mountVolume(path);
        else
            sendMyBroadcastWith2Extras(Constant.MOUNT_USB_ACTION, Constant.MOUNT_ENABLE_KEY, "1", Constant.MOUNT_POINT_KEY, path);
        return true;
    }

    //获取串口的绝对路径

    /**
     * @param uart，传入的是串口的序列号，比如串口0就传入TTYS0
     * @return 返回指定串口的绝对路径
     * @method getUartPath(String uart)
     * @description 获取串口的绝对路径
     * @date 20180602
     * @author sky
     */
    public String getUartPath(String uart) {
        if (isNewApi && version >= 6)
            return mNewApi.getUartPath(uart);
        else {
            if (!TextUtils.isEmpty(uart)) {
                if (uart.toUpperCase().equals("TTYS0")) {
                    return "/dev/ttyS0";
                }

                if (uart.toUpperCase().equals("TTYS1")) {
                    return "/dev/ttyS1";
                }

                if (uart.toUpperCase().equals("TTYS2")) {
                    return "/dev/ttyS2";
                }

                if (uart.toUpperCase().equals("TTYS3")) {
                    return "/dev/ttyS3";
                }

                if (uart.toUpperCase().equals("TTYS4")) {
                    return "/dev/ttyS4";
                }

                if (uart.toUpperCase().equals("TTYS5")) {
                    return "/dev/ttyS5";
                }
            }
            return "";
        }
    }

    //设置USB 的电源， 返回true表示设置成功, 目前只是针对OTG口起作用
    //value 0 表示关
    //value 1 表示开
    public boolean setUsbPower(int type, int num, int value) {
        Utils.do_exec("busybox echo " + value + " > " + Constant.USB_OTG_IO);
        return true;
    }

    public String getGPIOStatus(String path) {
        return GPIOUtils.readGpioPG("/sys/devices/misc_power_en.23/" + path);
    }

    public void setGPIOStatus(String value, String path) {
        try {
            GPIOUtils.writeIntFileUnder7(value, "/sys/devices/misc_power_en.23/" + path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isRk3128() {
        return "rk3128".endsWith(getAndroidModle());
    }

    // 读取IO口的状态，返回0或者1
    public int readGpioValue(int io) {
        return GPIOUtils.readGpio(io);
    }

    //设置IO口的状态， value为0或者1
    public void setGpioValue(int io, int value) {
        // 外部传入的IO值只能是0或者1
        if (value == 0 || value == 1) {
            GPIOUtils.writeGpio(io, value);
        }
    }

    // 开关耳机麦克风, value 0 关， value 1 开
    public void setHeadsetMicOnOff(int value) {

    }

    // 设置时间  24小时制

    /**
     * @param year,传入需要设置的年月日时分秒，其中月份从1-12，时间按照24小时制
     * @method setTime(int year, int month, int day, int hour, int minute, int sec)
     * @description 设置系统时间
     * @date 20180602
     * @author sky
     */
    public boolean setTime(int year, int month, int day, int hour, int minute, int sec) {////// ok
        if (isNewApi && version > 3)
            return mNewApi.setTime(TimeUtils.getTimeMills(year, month, day, hour, minute, sec));
        else {
            if (!setTimeAidl(TimeUtils.getTimeMills(year, month, day, hour, minute, sec)))
                sendMyBroadcastWithLongExtra(Constant.UPDATE_TIME_ACTION, Constant.UPDATE_TIME_KEY, TimeUtils.getTimeMills(year, month, day, hour, minute, sec));
        }
        return true;
    }

    public boolean setTime(long currentTimeMillis) {///////ok
        if (isNewApi && version > 3)
            return mNewApi.setTime(currentTimeMillis);
        else
            sendMyBroadcastWithLongExtra(Constant.UPDATE_TIME_ACTION, Constant.UPDATE_TIME_KEY, currentTimeMillis);
        return true;
    }

    // 执行su 命令
    /**
     * @method execSuCmd(String command)
     * @description 在root权限下运行shell命令
     * @date 20180602
     * @author sky
     */
    public boolean execSuCmd(String command) {///// ok
        return Utils.execFor7(command);
    }

    /**
     * 检测设备是否有su
     * @return
     */
    public boolean execSuCmd() {///// ok
        if (isNewApi && version >= 6)
            return mNewApi.execSuCmd();
        else
            return ShellUtils.checkRootPermission();
//        Utils.execFor7(command);
    }


    // 获取android logcat
    // path 指定输出logcat 的绝对路径，如 /mnt/internal_sd/logcat.txt

    /**
     * @param path，日志保存的路径
     * @method getAndroidLogcat(String path)
     * @description 获取安卓层日志
     * @date 20180602
     * @author sky
     */
    public void getAndroidLogcat(String path) {//// ok
        LogUtils.startLog(path);
    }

    public void stopAndroidLogcat() {//// ok
        LogUtils.stopLog();
    }


    //获取当前的上网类型
    //返回值 0表示以太网
    //返回值 1表示WIFI
    //返回值 2表示移动网络
    //返回值 -100表示未知网络

    /**
     * @return 返回int值，0表示以太网，1表示WIFI，2表示移动网络，-100表示未知网络
     * @method getCurrentNetType()
     * @description 获取当前网络类型
     * @date 20180602
     * @author sky
     */
    public int getCurrentNetType() {  // ok
        if (isNewApi && version >= 6)
            return mNewApi.getCurrentNetType();
        else {
            int realNetType = NetUtils.getNetWorkType(mContext);
            if (realNetType == 9)
                return Constant.NET_TYPE_ETH;
            if (realNetType == 1)
                return Constant.NET_TYPE_WIFI;
            if (realNetType == 0)
                return Constant.NET_TYPE_MOBILE;
            return Constant.NETWORK_UNKNOW;
        }
    }

    //开关一些外设的电源IO口
    // type外设类型
    public void setDevicePower(Device device, int values) {
        switch (device) {
            case HDMI:
                break;
            case LAN:
                break;
            case SPEAKER:
                break;
            case WIFI:
                break;
            case _3G: //3G模块
                break;
            case SD:
                break;
            case LED:
                break;
            default:
                break;
        }

    }

    // 获取当前屏幕数，默认返回1，有双屏时返回2

    /**
     * @return 设备连接几个屏就会返回几
     * @method getScreenNumber()
     * @description 获取当前设备连接的屏幕数
     * @date 20180602
     * @author sky
     */
    public int getScreenNumber() {
        mDisplayManager = (DisplayManager) mContext.getSystemService(
                Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();
        return displays.length;
    }

    /**
     * @return
     * @method getScreenNumber()
     * @description 获取屏幕分辨率
     * @date 20180602
     * @author sky
     */
    public Display getScreenResolution(int number) {
        mDisplayManager = (DisplayManager) mContext.getSystemService(
                Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();
        if (displays.length > 1)
            return displays[number];
        else
            return null;
    }

    // 获取HDMI IN 的状态
    // 返回0没有HDMI输入， 1有HDMI输入

    /**
     * @return HDMI有输出返回true，否则返回false
     * @method getHdmiinStatus()
     * @description 获取HDMI连接情况
     * @date 20180602
     * @author sky
     */
    public boolean getHdmiinStatus() {
        if (isNewApi && version > 3)
            return mNewApi.getHdmiinStatus();
        else
            return GPIOUtils.isHDMIOut();
    }

    //setPowerOnTime
    public void setSystemPowerOnTime(int year, int month, int day, int hourOfDay, int minute) {
        TimeUtils.setPowerOnTime(mContext, year, month, day, hourOfDay, minute);
    }

    //清除系统时间
    public void clearSystemPowerOnTime() {
        TimeUtils.clearPowerOnTime(mContext);
    }

    //设置开关机模式
    public void setSystemPowerOnOffMode(int value) {
        TimeUtils.setPowerOnMode(value);
    }

    //看门狗使能
//    public void setWatchDogEnable(int enable) {
//        TimeUtils.WatchDogEnable(enable);
//    }

    //喂狗
//    public void watchDogFeedTime() {
//        TimeUtils.WatchDogFeed();
//    }

    //控制自动确定日期和时间的开关

    /**
     * @param open，传入true就是打开开关，false关闭开关
     * @method switchAutoTime(boolean open)
     * @description 控制自动确定日期和时间的开关
     * @date 20180602
     * @author sky
     */
    public boolean switchAutoTime(boolean open) {
        if (isNewApi && version > 3)
            return mNewApi.switchAutoTime(open);
        else {
            Intent intent = new Intent("com.ys.switch_auto_set_time");
            intent.putExtra("switch_auto_time", open);
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
        return true;
    }

    /**
     * @param language，语言，如zh。country，国家，如CN
     * @method setLanguage(String language, String country)
     * @description 设置当前系统默认语言
     * @date 20190513
     * @author sky
     */
    public boolean setLanguage(String language, String country) {
        if (isNewApi && version > 3)
            return mNewApi.setLanguage(language, country);
        else {
            Intent intent = new Intent("com.ys.set_language");
            intent.putExtra("language", language);
            intent.putExtra("country", country);
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
        return true;
    }

    /**
     * @param path，日志保存的路径
     * @method getKmsgLog(String path)
     * @description 获取kernel层日志
     * @date 20180602
     * @author sky
     */
    public void getKmsgLog(String path) {
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        String lineText = null;
        List<String> txtLists = new ArrayList<>();
        try {
            process = Runtime.getRuntime().exec("dmesg");
            reader = new InputStreamReader(process.getInputStream());
            bufferedReader = new BufferedReader(reader);
            while ((lineText = bufferedReader.readLine()) != null) {
                txtLists.add(lineText);
            }
            StringBuffer buffer = new StringBuffer();
            for (String s : txtLists) {
                buffer.append(s + "\n");
            }
            LogUtils.saveToSDCard(path, buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param value，是按照1到100设置的，设置100即代表最大亮度，1代表最小亮度
     * @method changeScreenLight(int value)
     * @description 设置屏幕亮度
     * @date 20180828
     * @author sky
     */
    public boolean changeScreenLight(int value) {
        if (isNewApi && version > 3)
            return mNewApi.changeScreenLight(value);
        else
            YsFactory.getRK().changeScreenLight(mContext, value);
        return false;
    }

    /**
     * @param context，上下文对象。time，间隔时间（在间隔时间内没操作系统进入休眠）
     * @method setDormantInterval(Context context, long time)
     * @description 休眠时间的控制
     * @date 20181129
     * @author sky
     */
    public boolean setDormantInterval(Context context, long time) {
        if (isNewApi && version > 3)
            return mNewApi.setDormantInterval(time);
        else
            YsFactory.getRK().setDormantInterval(context, time);
        return true;
    }

    public boolean awaken() {
        if (isNewApi && version > 3)
            return mNewApi.setDormantInterval(Integer.MAX_VALUE);
        else
//        YsFactory.getRK().awaken();
            YsFactory.getRK().setDormantInterval(mContext, Integer.MAX_VALUE);
        return true;
    }

    //获取设置默认输入法是否成功

    /**
     * @param defaultInputMethod，需要设置的输入法的包名，例如谷歌拼音输入法"com.google.android.inputmethod.pinyin/.PinyinIME"
     * @return 设置成功返回true，失败返回false
     * @method isSetDefaultInputMethodSuccess(String defaultInputMethod)
     * @description 设置默认输入法，并判断是否设置成功
     * @date 20190228
     * @author sky
     */
    public boolean isSetDefaultInputMethodSuccess(String defaultInputMethod) {//ok
        Log.d(TAG, "isSetDefaultInputMethodSuccess isSetDefaultInputMethodSuccess");
        boolean isSuccess = false;
        if (igetMessage != null) {
            try {
                isSuccess = igetMessage.isSetDefaultInputMethodSuccess(defaultInputMethod);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    //获取当前的输入法

    /**
     * @return 返回当前输入法的包名，例如com.google.android.inputmethod.pinyin/.PinyinIME
     * @method getDefaultInputMethod()
     * @description 获取当前系统输入法
     * @date 20190228
     * @author sky
     */
    public String getDefaultInputMethod() {
        String defaultInputMethod = "";
        if (igetMessage != null) {
            try {
                defaultInputMethod = igetMessage.getDefaultInputMethod();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return defaultInputMethod;
    }

    /**
     * @param packagename，需要卸载的包名
     * @return 卸载成功返回true，否则返回false
     * @method unInstallApk(String apkPath)
     * @description 静默卸载apk，
     * @date 20210419
     * @author zouyuanhang
     */
    public boolean unInstallApk(String packagename) {
        boolean isSuccess = false;
        if (igetMessage != null) {
            try {
                isSuccess = igetMessage.unInstallApk(packagename);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    //获取CPU温度

    /**
     * @return 返回int值，单位是摄氏度
     * @method getCPUTemperature()
     * @description 获取当前系统CPU温度
     * @date 20190619
     * @author sky
     */
    public int getCPUTemperature() {
        if (Build.MODEL.contains("312") || Build.VERSION.SDK_INT == 22) {
            Toast.makeText(mContext, "暂不支持该功能", Toast.LENGTH_SHORT).show();
            return 0;
        }
        if (isNewApi && version > 3)
            return mNewApi.getCPUTemperature();
        else {
            return YsFactory.getRK().getCPUTemperature();
        }
    }

    /**
     * @param open，true，打开adb连接开关。false，关闭adb连接
     * @method setADBOpen(boolean open)
     * @description 打开或关闭adb连接
     * @date 20190628
     * @author sky
     */
    public boolean setADBOpen(boolean open) {
        if (isNewApi && version > 3)
            return mNewApi.setADBOpen(open);
        else {
            YsFactory.getRK().setADBOpen(open);
            if (Build.BOARD.contains("x301") && "28".equals(Build.VERSION.SDK))
                reboot();
        }
        return true;
    }

    /**
     * @param path，开机动画bootanimation.zip所在的绝对路径
     * @method replaceBootanimation(String path)
     * @description 替换开机动画
     * @date 20190628
     * @author sky
     */
    public boolean replaceBootanimation(final String path) {
//        if(Build.MODEL.contains("px30_e")||Build.MODEL.contains("rk3368")||Build.MODEL.contains("rk33128")&&"25".equals(Build.VERSION.SDK)){
//            String[] commands = new String[7];
//            commands[0] = "mount -o rw,remount -t ext4 /system";
//            commands[1] = "rm -rf oem/media/bootanimation.zip";
//            commands[2] = "cp  " + path + " oem/media/bootanimation.zip";
//            commands[3] = "chmod 644 oem/media/bootanimation.zip";
//            commands[4] = "sync";
//            commands[5] = "mount -o ro,remount -t ext4 /system";
//            commands[6] = "reboot";
//            for (int i = 0; i < commands.length; i++)
//                Utils.execFor7(commands[i]);
//        }
        if (isNewApi && version > 3)
            return mNewApi.replaceBootanimation(path);
        else {
            if (Build.VERSION.SDK_INT == 30) {
                try {
                    Utils.setValueToProp("persist.sys.command", "replace_bootanimation");
                    Utils.setValueToProp("service.bootanim.dirfile", path);
                    sleep(100);
                    Utils.setValueToProp("ctl.start", "sys_command");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (Build.VERSION.SDK_INT < 27) {
                String[] commands = new String[6];
                commands[0] = "mount -o rw,remount -t ext4 /system";
                commands[1] = "rm -rf system/media/bootanimation.zip";
                commands[2] = "cp  " + path + " system/media/bootanimation.zip";
                commands[3] = "chmod 644 system/media/bootanimation.zip";
                commands[4] = "sync";
                commands[5] = "mount -o ro,remount -t ext4 /system";
                for (int i = 0; i < commands.length; i++)
                    Utils.execFor7(commands[i]);
            } else if (Build.BOARD.contains("x301") && "28".equals(Build.VERSION.SDK)) {
                String[] commands = new String[6];
                commands[0] = "mount -o remount -o rw /";
                commands[1] = "rm -rf system/media/bootanimation.zip";
                commands[2] = "cp  " + path + " system/media/bootanimation.zip";
                commands[3] = "chmod 644 system/media/bootanimation.zip";
                commands[4] = "sync";
                commands[5] = "mount -o remount -o ro /";
                for (int i = 0; i < commands.length; i++)
                    Utils.execFor7(commands[i]);
            } else {
                String[] commands = new String[6];
                commands[0] = "mount -o rw,remount -t ext4 /oem";
                commands[1] = "rm -rf oem/media/bootanimation.zip";
                commands[2] = "cp  " + path + " oem/media/bootanimation.zip";
                commands[3] = "chmod 644 oem/media/bootanimation.zip";
                commands[4] = "sync";
                commands[5] = "mount -o ro,remount -t ext4 /oem";
                for (int i = 0; i < commands.length; i++)
                    Utils.execFor7(commands[i]);
            }
            reboot();
        }
        return true;
    }

    private void setScreenAndVoiceOpen(boolean open) {
        if ("25".equals(Build.VERSION.SDK)) {
            if (open) {
                GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "0");
//            GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"),"1");
            } else {
                GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "1");
                GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"), "1");
            }
        } else {
            if (open) {
                try {
                    GPIOUtils.writeIntFileUnder7("1", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    GPIOUtils.writeIntFileUnder7("0", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
                    GPIOUtils.writeIntFileUnder7("1", "/sys/bus/i2c/devices/2-0010/spkmode");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setStandByMode() {
        if ("25".equals(Build.VERSION.SDK)) {
            GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "1");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/green_led"), "0");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/red_led"), "0");
            GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"), "1");
        } else {
            try {
                GPIOUtils.writeIntFileUnder7("0", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
                GPIOUtils.writeIntFileUnder7("1", "/sys/bus/i2c/devices/2-0010/spkmode");
                GPIOUtils.writeIntFileUnder7("0", "/sys/devices/misc_power_en.23/green_led");
                GPIOUtils.writeIntFileUnder7("0", "/sys/devices/misc_power_en.23/red_led");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setNormalMode() {
        if ("25".equals(Build.VERSION.SDK)) {
            GPIOUtils.writeStringFileFor7(new File("/sys/class/backlight/backlight/bl_power"), "0");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/green_led"), "1");
            GPIOUtils.writeStringFileFor7(new File("/sys/devices/platform/misc_power_en/red_led"), "1");
//        GPIOUtils.writeStringFileFor7(new File("/sys/bus/i2c/devices/2-0010/spkmode"),"1");
        } else {
            try {
                GPIOUtils.writeIntFileUnder7("1", "/sys/devices/fb.8/graphics/fb0/pwr_bl");
//                GPIOUtils.writeIntFileUnder7("1","/sys/bus/i2c/devices/2-0010/spkmode");
                GPIOUtils.writeIntFileUnder7("1", "/sys/devices/misc_power_en.23/green_led");
                GPIOUtils.writeIntFileUnder7("1", "/sys/devices/misc_power_en.23/red_led");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void controlMainScreenBright(boolean isOpen) {
        if (isOpen)
            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio13/value"), "1");
        else
            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio13/value"), "0");
    }

    public void controlSecondScreenBright(boolean isOpen) {
        if (isOpen)
            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio230/value"), "1");
        else
            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio230/value"), "0");
    }

    public void controlVoice(boolean isOpen) {
        if (isOpen)
            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio226/value"), "0");
        else
            GPIOUtils.writeStringFileFor7(new File("/sys/class/gpio/gpio226/value"), "1");
    }

    private boolean upgradeRootPermission(String path) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + path;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        try {
            return process.waitFor() == 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param packageAndClassName，设置的Launcher的包名和启动类名，例如"com.android.launcher3/com.android.launcher3.Launcher"
     * @method setDefaultLauncher(String packageAndClassName)
     * @description 设置系统默认Launcher
     * @date 20190719
     * @author sky
     */
    public boolean setDefaultLauncher(String packageAndClassName) {
        if (isNewApi && version > 3)
            return mNewApi.setDefaultLauncher(packageAndClassName);
        else {
            Intent intent = new Intent("com.ys.setDefaultLauncher");
            intent.putExtra("defaultLauncher", packageAndClassName);
            intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
        return true;
    }

    /**
     * @param powerOnTime，开机时间，例如{8,30}。powerOffTime，关机时间，例如{18,30}。 weekdays，周一到周日工作状态,1为开机，0为不开机。例如{1,1,1,1,1,0,0}，是指周一到周五执行开关机
     * @method setPowerOnOffWithWeekly(int[] powerOnTime, int[] powerOffTime, int[] weekdays)
     * @description 周模式设置定时开关机，一天只有一组开关机时间
     * @date 20200113
     * @author sky
     */
    public boolean setPowerOnOffWithWeekly(int[] powerOnTime, int[] powerOffTime, int[] weekdays) {
        if (isNewApi && version >= 6) {
            Log.d(TAG, "poweron:" + Arrays.toString(powerOnTime) + "/ poweroff:" + Arrays.toString(powerOffTime) + "/weekday:" + Arrays.toString(weekdays));
            return mNewApi.setPowerOnOffWithWeekly(powerOnTime, powerOffTime, weekdays);
        }
        else {
            Intent intent = new Intent("android.intent.action.setyspoweronoff");
            intent.putExtra("timeon", powerOnTime);
            intent.putExtra("timeoff", powerOffTime);
            intent.putExtra("wkdays", weekdays);
            intent.putExtra("enable", true); //使能开关机功能，设为 false,则为关闭
            intent.setPackage(Constant.POWER_ON_OFF_PACKAGENAME);
            mContext.sendBroadcast(intent);
            Log.d(TAG, "poweron:" + Arrays.toString(powerOnTime) + "/ poweroff:" + Arrays.toString(powerOffTime) + "/weekday:" + Arrays.toString(weekdays));
            return true;
        }
        }

    /**
     * @param powerOnTime，开机时间，例如{2020,1,10,20,48}，powerOffTime，关机时间，例如{2020,1,10,20,38}。
     * @method setPowerOnOff(int[] powerOnTime, int[] powerOffTime)
     * @description 设置一组定时开关机时间数据，需要传入年月日时分
     * @date 20200113
     * @author sky
     */
    public boolean  setPowerOnOff(int[] powerOnTime, int[] powerOffTime) {
        if (isNewApi && version >= 6)
            return mNewApi.setPowerOnOff(powerOnTime,powerOffTime);
        else {
            Intent intent = new Intent(Constant.INTENT_ACTION_POWERONOFF);
            intent.putExtra("timeon", powerOnTime);
            intent.putExtra("timeoff", powerOffTime);
            intent.putExtra("enable", true); //使能开关机功能，设为 false,则为关闭
            intent.setPackage(Constant.POWER_ON_OFF_PACKAGENAME);
            mContext.sendBroadcast(intent);
            Log.d(TAG, "poweron:" + Arrays.toString(powerOnTime) + "/ poweroff:" + Arrays.toString(powerOffTime));
            return true;
        }
    }

    /**
     * @return "本地模式"是指在定时开关机本地设置的开关机时间。"网络周模式"是指用广播的方式调用setPowerOnOffWithWeekly方法。
     * "网络每组模式"是指用广播的方式调用setPowerOnOff方法。
     * @method getPowerOnMode()
     * @description 获取定时开关机的模式
     * @date 20200113
     * @author sky
     */
    public String getPowerOnMode() {
        if (isNewApi && version >= 6)
            return mNewApi.getPowerOnMode();
        else {
            String mode = Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONMODE);
            return mode;
        }
//        if ("2".equals(mode))
//            return "网络周模式";
//        else if ("0".equals(mode))
//            return "本地模式";
//        else
//            return "网络每组模式";
    }

    /**
     * @return 返回当前设置的开机时间，例如202001132025，是指2020年1月13号20:25开机
     * @method getPowerOnTime()
     * @description 获取当前设备的开机时间
     * @date 20200113
     * @author sky
     */
    public String getPowerOnTime() {
        if (isNewApi && version >= 6)
            return mNewApi.getPowerOnTime();
        else {
            String mode = Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONMODE);
            if ("2".equals(mode))
                return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONTIME_2);
            else
                return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONTIME);
        }
    }

    /**
     * @return 返回当前设置的关机时间，例如202001132020，是指2020年1月13号20:20关机
     * @method getPowerOffTime()
     * @description 获取当前设备的关机时间
     * @date 20200113
     * @author sky
     */
    public String getPowerOffTime() {
        if (isNewApi && version >= 6)
            return mNewApi.getPowerOffTime();
        else {
            String mode = Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONMODE);
            if ("2".equals(mode))
                return Utils.getValueFromProp(Constant.PERSIST_SYS_POWEROFFTIME_2);
            else
                return Utils.getValueFromProp(Constant.PERSIST_SYS_POWEROFFTIME);
        }
    }

    /**
     * @return 返回设备上一次的开机时间，例如202001132025，是指在2020年1月13号20:25执行了开机操作
     * @method getLastestPowerOnTime()
     * @description 获取设备上一次执行过的开机时间
     * @date 20200113
     * @author sky
     */
    public String getLastestPowerOnTime() {
        if (isNewApi && version >= 6)
            return mNewApi.getLastestPowerOnTime();
        else
        return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONTIMEPER);
    }

    /**
     * @return 返回设备上一次的关机时间，例如202001132020，是指在2020年1月13号20:20执行了关机操作
     * @method getLastestPowerOffTime()
     * @description 获取设备上一次执行过的关机时间
     * @date 20200113
     * @author sky
     */
    public String getLastestPowerOffTime() {
        if (isNewApi && version >= 6)
            return mNewApi.getLastestPowerOffTime();
        else
            return Utils.getValueFromProp(Constant.PERSIST_SYS_POWEROFFTIMEPER);
    }

    /**
     * @method clearPowerOnOffTime()
     * @description 清除定时开关机时间
     * @date 20200113
     * @author sky
     */
    public boolean clearPowerOnOffTime() {
        if (isNewApi && version >= 6)
            return mNewApi.clearPowerOnOffTime();
        else {
            Intent intent = new Intent(Constant.INTENT_ACTION_CLEARONTIME);
            intent.setPackage(Constant.POWER_ON_OFF_PACKAGENAME);
            mContext.sendBroadcast(intent);
            return true;
        }
    }

    /**
     * @return 设置了定时开关机返回true，否则返回false
     * @method isSetPowerOnTime()
     * @description 获取设备是否设置了定时开关机
     * @date 20200113
     * @author sky
     */
    public boolean isSetPowerOnTime() {
        if (isNewApi && version >= 6)
            return mNewApi.isSetPowerOnTime();
        else
        return !"0".equals(getPowerOnTime());
    }

    /**
     * @return 返回定时开关机版本号，例如YS_1.0_20190217
     * @method getVersion()
     * @description 获取定时开关机版本号
     * @date 20200113
     * @author sky
     */
    public String getVersion() {
        if (isNewApi && version >= 6)
            return mNewApi.getVersion();
        else
        return Utils.getValueFromProp(Constant.PERSIST_SYS_POWERONOFF_VERSION);
    }

    /**
     * @method upgradeRootPermissionForExport()
     * @description 给export申请权限，建议在开机广播或应用启动时就执行
     * @date 20200213
     * @author sky
     */
    public void upgradeRootPermissionForExport() {
        GPIOUtils.upgradeRootPermissionForExport();
    }

    /**
     * @return 配置成功返回true，配置失败返回false
     * @method exportGpio(int gpio)
     * @description 根据具体的索引值配置export路径，建议在开机广播或应用启动时就执行
     * @date 20200213
     * @author sky
     */
    public boolean exportGpio(int gpio) {
        return GPIOUtils.exportGpio(gpio);
    }

    /**
     * @method upgradeRootPermissionForGpio(int gpio)
     * @description 给配置好的gpio路径申请权限，建议在开机广播或应用启动时就执行，在exportGpio(int gpio)方法后执行
     * @date 20200213
     * @author sky
     */
    public void upgradeRootPermissionForGpio(int gpio) {
        GPIOUtils.upgradeRootPermissionForGpio(gpio);
    }

    /**
     * @param gpio，所要操作的gpio索引值。arg，1代表设置为输入口，0代表设置为输出口。
     * @return 设置成功返回true，失败返回false
     * @method setGpioDirection(int gpio, int arg)
     * @description 设置io口的状态是输入或输出
     * @date 20200213
     * @author sky
     */
    public boolean setGpioDirection(int gpio, int arg) {
        if (isNewApi && version > 3)
            return mGpio.setGpioDirection(gpio, arg);
        else
            return GPIOUtils.setGpioDirection(gpio, arg);
    }

    /**
     * @param gpio，gpio的索引值
     * @return 输入口返回in，输出口返回out
     * @method getGpioDirection(int gpio)
     * @description 根据具体的gpio索引值获取当前gpio的状态是输入还是输出
     * @date 20200213
     * @author sky
     */
    public String getGpioDirection(int gpio) {
        if (isNewApi && version > 3)
            return mGpio.getGpioDirection(gpio);
        else
            return GPIOUtils.getGpioDirection(gpio);
    }

    /**
     * @param gpio，gpio的索引值。Arg，高电平--1，低电平--0
     * @return 写入成功返回true，失败返回false
     * @method writeGpioValue(int gpio, String arg)
     * @description 控制gpio电平，只针对输出口
     * @date 20200213
     * @author sky
     */
    public boolean writeGpioValue(int gpio, String arg) {
        if (isNewApi && version > 3)
            return mGpio.setGpioValue(gpio, String.valueOf(arg));
        else
            return GPIOUtils.writeGpioValue(gpio, arg);
    }

    /**
     * @param gpio，gpio的索引值
     * @return 高电平返回1，低电平返回0
     * @method getGpioValue(int gpio)
     * @description 获取当前gpio的电平
     * @date 20200213
     * @author sky
     */
    public String getGpioValue(int gpio) {
        if (isNewApi && version > 3)
            return mGpio.getGpioValue(gpio);
        else
            return GPIOUtils.getGpioValue(gpio);
    }

    /**
     * @param * @param packagname
     * @return
     * @method selfStart
     * @description 开机自启app
     * @date: 2021/1/11
     * @author: zouyuanhang
     */
    public boolean selfStart(String packagname) {
        if (isNewApi && version >= 6)
            return mNewApi.selfStart(packagname);
        else {
            Utils.setValueToProp("persist.sys.openapp", packagname);
            return true;
        }
    }

    /**
     * @param * @param null
     * @return
     * @method
     * @description 获取sn号
     * @date: 2021/1/11
     * @author: zouyuanhang
     */
    @SuppressLint("MissingPermission")
    public String getSn() {
        if (Build.VERSION.SDK_INT >= 28) {
            String defaultSn = "";
            if (igetMessage != null) {
                try {
                    defaultSn = igetMessage.getSerial();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            return defaultSn;
        } else
            return Build.SERIAL;
    }

    /**
     * @param * @param
     * @return
     * @method getVendorID
     * @description 获取厂商识别码
     * @date: 2021/1/11
     * @author: zouyuanhang
     */
    public String getVendorID() {
        if (isNewApi && version > 3)
            return mNewApi.getVendorID();
        else {
            if ("".equals(Utils.getValueFromProp("persist.ys.factroy.code")))
                return "YSTA6212";
            else
                return Utils.getValueFromProp("persist.ys.factroy.code");
        }
    }

    /**
     * @param * @param
     * @return 返回ture代表隐藏 false代表显示
     * @method getstatusBar
     * @description 获取状态栏状态
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean getStatusBar() {
        if (isNewApi && version > 3)
            return mNewApi.getStatusBar();
        else {
            if (Utils.getValueFromProp("persist.sys.hidetopbar").equals("0"))
                return true;
            else
                return false;
        }
    }

    /**
     * @param * @param flag 隐藏状态栏传入false，显示传入true
     * @return
     * @method setStatusBar
     * @description 显示隐藏状态栏
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean hideStatusBar(boolean flag) {
        if (isNewApi && version > 3)
            return mNewApi.hideStatusBar(flag);
        else {
            Intent intent = null;
            if (flag) {
                Utils.setValueToProp("persist.sys.hidetopbar", "1");
                if (getAndroidModle().equals("px30_e") || Build.VERSION.SDK_INT > 27) {
                    intent = new Intent("com.ys.showStatusBarWindow");
                    intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
                } else {
                    intent = new Intent("android.intent.action.showStatusBarWindow");
                }
            } else {
                Utils.setValueToProp("persist.sys.hidetopbar", "0");
//            Utils.setValueToProp("persist.sys.disexpandbar", "1");
                if (getAndroidModle().equals("px30_e") || Build.VERSION.SDK_INT > 27) {
                    intent = new Intent("com.ys.hideStatusBarWindow");
                    intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
                } else {
                    intent = new Intent("android.intent.action.hideStatusBarWindow");
                }
            }
            mContext.sendBroadcast(intent);
        }
        return true;
    }

    /**
     * @param * @param value
     * @return
     * @method setDpi
     * @description 设置DPI
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean setDpi(int value) {
        if (isNewApi && version > 3)
            return mNewApi.setDpi(value);
        else {
            int densityValue = 0;
            if (value == 0)
                densityValue = 160;
            else if (value == 1)
                densityValue = 240;
            else if (value == 2)
                densityValue = 320;
            else if (value == 3)
                densityValue = 480;
            else
                densityValue = 160;

            sendMyBroadcastWithLongExtra("com.ys.set_dpi", "densityDpi", densityValue);
            //        Utils.execFor7("wm density "+densityValue);
        }
        return true;
    }

    /**
     * @param * @param
     * @return 返回dpi
     * @method getDpi
     * @description 获取DPI
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public String getDpi() {
        if (isNewApi && version >= 6)
            return mNewApi.getDpi();
        else {
            DisplayMetrics dm = new DisplayMetrics();
            Activity activity = (Activity) mContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            String densityValue = null;
            if (0.75 == density)
                densityValue = "120";
            if (1.0 == density)
                densityValue = "160";
            if (1.5 == density)
                densityValue = "240";
            if (2.0 == density)
                densityValue = "320";
            if (3.0 == density)
                densityValue = "480";
            return densityValue;
        }
    }

    /**
     * @param * @param
     * @return 主屏幕类型
     * @method getHomeScreenType
     * @description 获取主屏幕类型
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public String getHomeScreenType() {
        if (VersionUtils.getAndroidModle().contains("rk3128"))
            return "3128芯片暂无此功能";
        if (isNewApi && version > 3)
            return mNewApi.getHomeScreenType();
        else {
            if (VersionUtils.getAndroidModle().contains("rk3568") && "30".equals(Build.VERSION.SDK) || VersionUtils.getAndroidModle().contains("rk3566") || "28".equals(Build.VERSION.SDK))
                return Utils.getValueFromProp("vendor.hwc.device.primary");
            else
                return Utils.getValueFromProp("sys.hwc.device.main");
        }
    }

    /**
     * @param * @param
     * @return 副屏幕类型
     * @method getSecondaryScreenType
     * @description 获取副屏幕类型
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public String getSecondaryScreenType() {
        if (VersionUtils.getAndroidModle().contains("rk3128"))
            return "3128芯片暂无此功能";
        if (VersionUtils.getAndroidModle().contains("rk3368"))
            return "3368芯片暂无此功能";
        if (isNewApi && version > 3)
            return mNewApi.getSecondaryScreenType();
        else {
            if (VersionUtils.getAndroidModle().contains("rk3568") && "30".equals(Build.VERSION.SDK) || "28".equals(Build.VERSION.SDK) && VersionUtils.getAndroidModle().contains("rk3288"))
                return Utils.getValueFromProp("vendor.hwc.device.extend");
            else
                return Utils.getValueFromProp("sys.hwc.device.aux");
        }
    }

    /**
     * @param *     @param packageName 需要守护应用的包名 none代表无
     * @param value 守护的时间 0 = 30秒 ，1 = 60秒 2=180秒默认30秒
     * @return
     * @method daemon
     * @description 设置守护进程
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean daemon(String packageName, int value) {
        if (isNewApi && version > 3)
            return mNewApi.daemon(packageName, value);
        else {
            String time = "30秒";
            Utils.setValueToProp("persist.sys.daemonsapp", packageName);
            if (value == 0)
                time = "30秒";
            else if (value == 1)
                time = "60秒";
            else if (value == 2)
                time = "180秒";
            Utils.setValueToProp("persist.sys.daemonsapp_time", time);
            Intent intent = new Intent("com.ys.action.GuardService").setPackage("com.ys.ys_receiver");
            mContext.startService(intent);
        }
        return true;
    }

    /**
     * @param * @param open true 打开 false 关闭
     * @return
     * @method setNetworkAdb
     * @description 打开网络adb
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean setNetworkAdb(boolean open) {
        if (isNewApi && version > 3)
            return mNewApi.setNetworkAdb(open);
        else {
            if (open)
                Utils.setValueToProp("persist.internet.adb.enable", "1");
            else
                Utils.setValueToProp("persist.internet.adb.enable", "0");
        }
        return true;
    }

    /**
     * @param *           @param isopen true 启用白名单 false 不启用
     * @param packageName 需要增加白名单的应用包名，如需删除应用包名可用Utils.removeFileData(packageName,true)
     * @return
     * @method setAppInstallWhitelist
     * @description 添加应用安装白名单
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean setAppInstallWhitelist(String isopen, String packageName) {
        if (isNewApi && version > 3)
            return mNewApi.setAppInstallWhitelist(isopen, packageName);
        else {
            Utils.setValueToProp("persist.neo.WhiteList", isopen);
            Utils.writeFileData(packageName, false);
        }
        return true;
    }

    /**
     * @param *           @param isopen true 启用黑名单 false 不启用
     * @param packageName 需要增加黑名单的应用包名，如需删除应用包名可用Utils.removeFileData(packageName,true)
     * @return
     * @method setAppInstallWhitelist
     * @description 添加应用安装黑名单
     * @date: 2021/1/12
     * @author: zouyuanhang
     */
    public boolean setAppInstallBlacklist(String isopen, String packageName) {
        if (isNewApi && version > 3)
            return mNewApi.setAppInstallBlacklist(isopen, packageName);
        else {
            Utils.setValueToProp("persist.neo.blackList", isopen);
            Utils.writeFileData(packageName, true);
        }
        return true;
    }

    public boolean removeFileData(String packageName, boolean black) {
        if (isNewApi && version > 3)
            return mNewApi.removeFileData(packageName, black);
        else
            Utils.removeFileData(packageName, black);
        return true;
    }


//获取以太网的IP地址
//    public String getEthIPAddress() {//ok
//        return NetUtils.getEthIPAddress();
//    }

    //设置静态IP
    //返回以太网IP地址
//    public String setEthIPAddress(String IPaddr, String mask, String gateWay, String dns1, String dns2) {//ok
//        NetUtils.setStaticIP(mContext, IPaddr, gateWay, mask, dns1, dns2);
//        return NetUtils.getEthIPAddress();
//    }
}
