// IgetStaticIP.aidl
package com.ys.myapi;

// Declare any non-default types here with import statements

interface INewApi {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getFirmwareVersion();
    boolean rotateScreen(String degree);
    boolean getNavBarHideState();
    boolean hideNavBar(boolean hide);
    boolean isSlideShowNavBarOpen();
    boolean setSlideShowNavBar(boolean flag);
    boolean isSlideShowNotificationBarOpen();
    boolean setSlideShowNotificationBar(boolean flag);
    int[] getDisplayHeight_width();
    boolean turnOffBackLight();
    boolean turnOnBackLight();
    boolean isBacklightOn();
    int getSystemBrightness();
    boolean turnOnHDMI();
    boolean turnOffHDMI();
    boolean upgradeSystem(String absolutePath);
    boolean setUpdateSystemWithDialog(boolean flag);
    boolean setUpdateSystemDelete(boolean flag);
    boolean rebootRecovery();
    boolean silentInstallApk(String apkPath, boolean isStartApk);
    boolean setEthMacAddress(String val);
    boolean setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2);
    boolean setDhcpIpAddress();
    boolean setPppoeDial(String userName, String password);
    boolean ethEnabled(boolean enable);
    boolean setSoftKeyboardHidden(boolean hidden);
    boolean unmountVolume(String path);
    boolean mountVolume(String path);
    boolean setTime(long currentTimeMillis);
    boolean getHdmiinStatus();
    boolean switchAutoTime(boolean open);
    boolean setLanguage(String language, String country);
    boolean changeScreenLight(int value);
    boolean setDormantInterval(long time);
    boolean awaken();
    boolean setADBOpen(boolean open);
    boolean replaceBootanimation(String path);
    boolean setDefaultLauncher(String packageAndClassName);
    boolean hideStatusBar(boolean flag);
    boolean setDpi(int value);
    String getHomeScreenType();
    String getSecondaryScreenType();
    boolean daemon(String packageName, int value);
    boolean setNetworkAdb(boolean open);
    int getCPUTemperature();
    String getVendorID();
    boolean getStatusBar();
    boolean setAppInstallWhitelist(String isopen, String packageName);
    boolean setAppInstallBlacklist(String isopen, String packageName);
    boolean removeFileData(String packageName,boolean black);
    void shutdown();
    void reboot();
    boolean changeSecondaryScreenLight(int value);
    int getSecondaryScreenBrightness();
    String getAndroidVersion();
    String getRunningMemory();
    String getInternalStorageMemory();
    String getKernelVersion();
    String getAndroidDisplay();
    String getCPUType();
    String getFirmwareDate();
    String getEthMacAddress();
    String getSDcardPath();
    String getUSBStoragePath(int num);
    String getUartPath(String uart);
    boolean execSuCmd();
    int getCurrentNetType();
    boolean setPowerOnOffWithWeekly(in int[] powerOnTime, in int[] powerOffTime,in int[] weekdays);
    boolean setPowerOnOff(in int[] powerOnTime,in int[] powerOffTime);
    String getPowerOnMode();
    String getPowerOnTime();
    String getPowerOffTime();
    String getLastestPowerOnTime();
    String getLastestPowerOffTime();
    boolean clearPowerOnOffTime();
    boolean isSetPowerOnTime();
    String getVersion();
    boolean selfStart(String packagname);
    String getDpi();
}