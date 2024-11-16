package com.ys.rkapi;

import android.os.RemoteException;

import com.ys.myapi.INewApi;

public class NewApi {
    private INewApi mINewApi;
    public void setNewApi(INewApi newApi) {
        mINewApi = newApi;
    }
    public int getFirmwareVersion(){
        if (mINewApi!=null){
            try {
                return mINewApi.getFirmwareVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public boolean rotateScreen(String degree){
        if (mINewApi!=null){
            try {
                return mINewApi.rotateScreen(degree);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean getNavBarHideState(){
        if (mINewApi!=null){
            try {
                return mINewApi.getNavBarHideState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean hideNavBar(boolean hide){
        if (mINewApi!=null){
            try {
                return mINewApi.hideNavBar(hide);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean isSlideShowNavBarOpen(){
        if (mINewApi!=null){
            try {
                return mINewApi.isSlideShowNavBarOpen();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setSlideShowNavBar(boolean flag){
        if (mINewApi!=null){
            try {
                return mINewApi.setSlideShowNavBar(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean isSlideShowNotificationBarOpen(){
        if (mINewApi!=null){
            try {
                return mINewApi.isSlideShowNotificationBarOpen();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean setSlideShowNotificationBar(boolean flag){
        if (mINewApi!=null){
            try {
                return mINewApi.setSlideShowNotificationBar(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int[] getDisplayHeight_width(){
        if (mINewApi!=null){
            try {
                return mINewApi.getDisplayHeight_width();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean turnOffBackLight(){
        if (mINewApi!=null){
            try {
                return mINewApi.turnOffBackLight();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean turnOnBackLight(){
        if (mINewApi!=null){
            try {
                return mINewApi.turnOnBackLight();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean isBacklightOn(){
        if (mINewApi!=null){
            try {
                return mINewApi.isBacklightOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public int getSystemBrightness(){
        if (mINewApi!=null){
            try {
                return mINewApi.getSystemBrightness();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    public boolean turnOnHDMI(){
        if (mINewApi!=null){
            try {
                return mINewApi.turnOnHDMI();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean turnOffHDMI(){
        if (mINewApi!=null){
            try {
                return mINewApi.turnOffHDMI();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean upgradeSystem(String absolutePath){
        if (mINewApi!=null){
            try {
                return mINewApi.upgradeSystem(absolutePath);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setUpdateSystemWithDialog(boolean flag){
        if (mINewApi!=null){
            try {
                return mINewApi.setUpdateSystemWithDialog(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setUpdateSystemDelete(boolean flag){
        if (mINewApi!=null){
            try {
                return mINewApi.setUpdateSystemDelete(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean rebootRecovery(){
        if (mINewApi!=null){
            try {
                return mINewApi.rebootRecovery();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean silentInstallApk(String apkPath, boolean isStartApk){
        if (mINewApi!=null){
            try {
                return mINewApi.silentInstallApk(apkPath,isStartApk);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setEthMacAddress(String val){
        if (mINewApi!=null){
            try {
                return mINewApi.setEthMacAddress(val);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setStaticEthIPAddress(String IPaddr, String gateWay, String mask, String dns1, String dns2){
        if (mINewApi!=null){
            try {
                return mINewApi.setStaticEthIPAddress(IPaddr,gateWay,mask,dns1,dns2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setDhcpIpAddress(){
        if (mINewApi!=null){
            try {
                return mINewApi.setDhcpIpAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setPppoeDial(String userName, String password){
        if (mINewApi!=null){
            try {
                return mINewApi.setPppoeDial(userName,password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean ethEnabled(boolean enable){
        if (mINewApi!=null){
            try {
                return mINewApi.ethEnabled(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setSoftKeyboardHidden(boolean hidden){
        if (mINewApi!=null){
            try {
                return mINewApi.setSoftKeyboardHidden(hidden);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean unmountVolume(String path){
        if (mINewApi!=null){
            try {
                return mINewApi.unmountVolume(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean mountVolume(String path){
        if (mINewApi!=null){
            try {
                return mINewApi.mountVolume(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setTime(long currentTimeMillis){
        if (mINewApi!=null){
            try {
                return mINewApi.setTime(currentTimeMillis);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean getHdmiinStatus(){
        if (mINewApi!=null){
            try {
                return mINewApi.getHdmiinStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean switchAutoTime(boolean open){
        if (mINewApi!=null){
            try {
                return mINewApi.switchAutoTime(open);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setLanguage(String language, String country){
        if (mINewApi!=null){
            try {
                return mINewApi.setLanguage(language,country);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean changeScreenLight(int value){
        if (mINewApi!=null){
            try {
                return mINewApi.changeScreenLight(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setDormantInterval(long time){
        if (mINewApi!=null){
            try {
                return mINewApi.setDormantInterval(time);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean awaken(){
        if (mINewApi!=null){
            try {
                return mINewApi.awaken();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setADBOpen(boolean open){
        if (mINewApi!=null){
            try {
                return mINewApi.setADBOpen(open);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean replaceBootanimation(String path){
        if (mINewApi!=null){
            try {
                return mINewApi.replaceBootanimation(path);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setDefaultLauncher(String packageAndClassName){
        if (mINewApi!=null){
            try {
                return mINewApi.setDefaultLauncher(packageAndClassName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean hideStatusBar(boolean flag){
        if (mINewApi!=null){
            try {
                return mINewApi.hideStatusBar(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setDpi(int value){
        if (mINewApi!=null){
            try {
                return mINewApi.setDpi(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public String getHomeScreenType(){
        if (mINewApi!=null){
            try {
                return mINewApi.getHomeScreenType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public String getSecondaryScreenType(){
        if (mINewApi!=null){
            try {
                return mINewApi.getSecondaryScreenType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean daemon(String packageName, int value){
        if (mINewApi!=null){
            try {
                return mINewApi.daemon(packageName,value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public boolean setNetworkAdb(boolean open){
        if (mINewApi!=null){
            try {
                return mINewApi.setNetworkAdb(open);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getCPUTemperature(){
        if (mINewApi!=null){
            try {
                return mINewApi.getCPUTemperature();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getVendorID(){
        if (mINewApi!=null){
            try {
                return mINewApi.getVendorID();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean getStatusBar(){
        if (mINewApi!=null){
            try {
                return mINewApi.getStatusBar();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean setAppInstallWhitelist(String isopen, String packageName){
        if (mINewApi!=null){
            try {
                return mINewApi.setAppInstallWhitelist(isopen,packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean setAppInstallBlacklist(String isopen, String packageName){
        if (mINewApi!=null){
            try {
                return mINewApi.setAppInstallBlacklist(isopen,packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean removeFileData(String packageName,boolean black){
        if (mINewApi!=null){
            try {
                return mINewApi.removeFileData(packageName,black);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void shutdown(){
        if (mINewApi!=null){
            try {
                mINewApi.shutdown();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void reboot(){
        if (mINewApi!=null){
            try {
                mINewApi.reboot();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean changeSecondaryScreenLight(int value){
        if (mINewApi!=null){
            try {
                return mINewApi.changeSecondaryScreenLight(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getSecondaryScreenBrightness(){
        if (mINewApi!=null){
            try {
                return mINewApi.getSecondaryScreenBrightness();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getAndroidVersion(){
        if (mINewApi!=null){
            try {
                return mINewApi.getAndroidVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getRunningMemory(){
        if (mINewApi!=null){
            try {
                return mINewApi.getRunningMemory();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getInternalStorageMemory(){
        if (mINewApi!=null){
            try {
                return mINewApi.getInternalStorageMemory();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getKernelVersion(){
        if (mINewApi!=null){
            try {
                return mINewApi.getKernelVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getAndroidDisplay(){
        if (mINewApi!=null){
            try {
                return mINewApi.getAndroidDisplay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getCPUType(){
        if (mINewApi!=null){
            try {
                return mINewApi.getCPUType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getFirmwareDate(){
        if (mINewApi!=null){
            try {
                return mINewApi.getFirmwareDate();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getEthMacAddress(){
        if (mINewApi!=null){
            try {
                return mINewApi.getEthMacAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getSDcardPath(){
        if (mINewApi!=null){
            try {
                return mINewApi.getSDcardPath();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getUSBStoragePath(int num){
        if (mINewApi!=null){
            try {
                return mINewApi.getUSBStoragePath(num);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getUartPath(String uart){
        if (mINewApi!=null){
            try {
                return mINewApi.getUartPath(uart);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean execSuCmd(){
        if (mINewApi!=null){
            try {
                return mINewApi.execSuCmd();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getCurrentNetType(){
        if (mINewApi!=null){
            try {
                return mINewApi.getCurrentNetType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public boolean setPowerOnOffWithWeekly(int[] powerOnTime, int[] powerOffTime,int[] weekdays){
        if (mINewApi!=null){
            try {
                return mINewApi.setPowerOnOffWithWeekly(powerOnTime,powerOffTime,weekdays);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean setPowerOnOff(int[] powerOnTime,int[] powerOffTime){
        if (mINewApi!=null){
            try {
                return mINewApi.setPowerOnOff(powerOnTime,powerOffTime);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean clearPowerOnOffTime(){
        if (mINewApi!=null){
            try {
                return mINewApi.clearPowerOnOffTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean isSetPowerOnTime(){
        if (mINewApi!=null){
            try {
                return mINewApi.isSetPowerOnTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean selfStart(String packagname){
        if (mINewApi!=null){
            try {
                return mINewApi.selfStart(packagname);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    public String getPowerOnMode(){
        if (mINewApi!=null){
            try {
                return mINewApi.getPowerOnMode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getPowerOnTime(){
        if (mINewApi!=null){
            try {
                return mINewApi.getPowerOnTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getPowerOffTime(){
        if (mINewApi!=null){
            try {
                return mINewApi.getPowerOffTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLastestPowerOnTime(){
        if (mINewApi!=null){
            try {
                return mINewApi.getLastestPowerOnTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getLastestPowerOffTime(){
        if (mINewApi!=null){
            try {
                return mINewApi.getLastestPowerOffTime();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getVersion(){
        if (mINewApi!=null){
            try {
                return mINewApi.getVersion();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getDpi(){
        if (mINewApi!=null){
            try {
                return mINewApi.getDpi();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
