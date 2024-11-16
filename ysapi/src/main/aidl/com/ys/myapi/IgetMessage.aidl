// IgetStaticIP.aidl
package com.ys.myapi;
// Declare any non-default types here with import statements
import com.ys.myapi.INewApi;
import com.ys.myapi.IGpio;

interface IgetMessage {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getStaticIP();
    String getEthMode();
    boolean getEthStatus();
    boolean isAutoSyncTime();
    String getEthDns1();
    String getEthDns2();
    String getGateway();
    String getNetMask();
//    Bitmap Screenshot();
    boolean isSuccessScreenshot(String path);

    boolean isSetDefaultInputMethodSuccess(String defaultInputMethod);
    String getDefaultInputMethod();
    boolean isSuccessViceScreenshot(String path);
    boolean setCurrentTimeMillis(long currentTime);
    boolean unInstallApk(String packagename);
    String getDhcpIpAddress();
    String getSerial();
    INewApi getApi();
    IGpio getGpio();
    }