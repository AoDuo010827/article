package rkandroidapi.ys.com.rkandroidapi;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.ys.rkapi.MyManager;
import com.ys.rkapi.Utils.VersionUtils;

import java.io.File;


public class MainActivity extends Activity implements View.OnClickListener {

    private MyManager manager;
    private String STATICIP = "StaticIp";
    private String DHCP = "DHCP";
    private String IPADDR = "192.168.1.125";
    private String MASK = "255.255.255.0";
    private String GATEWAY = "192.168.1.1";
    private String DNS1 = "192.168.1.1";
    private String DNS2 = "0.0.0.0";
    private String TFPath;
    private String USBPath;
    int i ,j= 0;
    private String externalStoragePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = MyManager.getInstance(this);
        manager.bindAIDLService(this);

        manager.setConnectClickInterface(new MyManager.ServiceConnectedInterface() {
            @Override
            public void onConnect() {
                TextView textView = findViewById(R.id.text_version);
                if (Integer.valueOf(manager.getFirmwareVersion()) < 4)
                    textView.setText("正在使用旧版本api");
                else
                    textView.setText("正在使用新版本api");
            }
        });
        externalStoragePath = Environment.getExternalStorageDirectory().getPath();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mountReceiver, filter);
        findViewById(R.id.get_api_version).setOnClickListener(this);
        findViewById(R.id.get_device_type).setOnClickListener(this);
        findViewById(R.id.get_android_version).setOnClickListener(this);
        findViewById(R.id.get_firmware_size).setOnClickListener(this);
        findViewById(R.id.get_in_store_size).setOnClickListener(this);
        findViewById(R.id.get_firmware_sdk_version).setOnClickListener(this);
        findViewById(R.id.get_firmware_kernel_version).setOnClickListener(this);
        findViewById(R.id.get_device_firmware_version).setOnClickListener(this);
        findViewById(R.id.get_firmware_date).setOnClickListener(this);
        findViewById(R.id.get_cpu_type).setOnClickListener(this);

        //-----------------------------------------------------------------
        findViewById(R.id.shutdown).setOnClickListener(this);
        findViewById(R.id.reboot).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.nav_bar_state).setOnClickListener(this);
        findViewById(R.id.nav_bar_hide_show).setOnClickListener(this);
        findViewById(R.id.nav_bar_slide_state).setOnClickListener(this);
        findViewById(R.id.nav_bar_slide).setOnClickListener(this);
        findViewById(R.id.notification_bar_slide_state).setOnClickListener(this);
        findViewById(R.id.notification_bar_slide).setOnClickListener(this);
        findViewById(R.id.screen_shot).setOnClickListener(this);
        findViewById(R.id.vice_screen_shot).setOnClickListener(this);
        findViewById(R.id.get_width_height).setOnClickListener(this);
        findViewById(R.id.get_secondary_screen_width_height).setOnClickListener(this);
        findViewById(R.id.change_screen_bright).setOnClickListener(this);
        findViewById(R.id.get_brightness).setOnClickListener(this);
        findViewById(R.id.open_screen_bright).setOnClickListener(this);
        findViewById(R.id.close_screen_bright).setOnClickListener(this);
        findViewById(R.id.is_screen_light_on).setOnClickListener(this);
        findViewById(R.id.open_hdmi).setOnClickListener(this);
        findViewById(R.id.close_hdmi).setOnClickListener(this);
        findViewById(R.id.rotate_90).setOnClickListener(this);
        findViewById(R.id.rotate_0).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.update_img).setOnClickListener(this);
        findViewById(R.id.recovery).setOnClickListener(this);
        findViewById(R.id.silent_install).setOnClickListener(this);
        findViewById(R.id.silent_uninstall).setOnClickListener(this);
        findViewById(R.id.update_img_show_dialog).setOnClickListener(this);
        findViewById(R.id.update_img_not_show_dialog).setOnClickListener(this);
        findViewById(R.id.update_img_delete_file).setOnClickListener(this);
        findViewById(R.id.update_img_not_delete_file).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.get_eth_status).setOnClickListener(this);
        findViewById(R.id.get_eth_mode).setOnClickListener(this);
        findViewById(R.id.get_eth_mac).setOnClickListener(this);
        findViewById(R.id.set_eth_mac).setOnClickListener(this);
        findViewById(R.id.get_eth_ip).setOnClickListener(this);
        findViewById(R.id.set_eth_ip).setOnClickListener(this);
        findViewById(R.id.open_eth).setOnClickListener(this);
        findViewById(R.id.close_eth).setOnClickListener(this);
        findViewById(R.id.set_dhcp_eth).setOnClickListener(this);
        findViewById(R.id.get_mask).setOnClickListener(this);
        findViewById(R.id.get_gateway).setOnClickListener(this);
        findViewById(R.id.get_dns1).setOnClickListener(this);
        findViewById(R.id.get_dns2).setOnClickListener(this);
//        findViewById(R.id.set_pppoe_dial).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.externel_sd_path).setOnClickListener(this);
        findViewById(R.id.externel_u_path).setOnClickListener(this);
        findViewById(R.id.uninstall_externel).setOnClickListener(this);
        findViewById(R.id.mount_externel).setOnClickListener(this);
        findViewById(R.id.read_eeprom).setOnClickListener(this);
        findViewById(R.id.write_eeprom).setOnClickListener(this);
        findViewById(R.id.get_uartPath).setOnClickListener(this);
        //-----------------------------------------------------------------
        findViewById(R.id.set_time).setOnClickListener(this);
        findViewById(R.id.su_order).setOnClickListener(this);
        findViewById(R.id.net_type).setOnClickListener(this);
//        findViewById(R.id.screen_num).setOnClickListener(this);
        findViewById(R.id.hdmi).setOnClickListener(this);
        //------------------------------------------------------------------
        findViewById(R.id.switch_on_time).setOnClickListener(this);
        findViewById(R.id.switch_off_time).setOnClickListener(this);
        findViewById(R.id.is_auto_time).setOnClickListener(this);
        findViewById(R.id.get_kernel_log).setOnClickListener(this);
        findViewById(R.id.get_android_log).setOnClickListener(this);
        findViewById(R.id.stop_android_log).setOnClickListener(this);
        findViewById(R.id.open_softkeyboard).setOnClickListener(this);
        findViewById(R.id.set_default_inputmethod).setOnClickListener(this);
        findViewById(R.id.get_default_inputmethod).setOnClickListener(this);
        findViewById(R.id.close_softkeyboard).setOnClickListener(this);
        findViewById(R.id.set_dormant_interval).setOnClickListener(this);
//        findViewById(R.id.awaken).setOnClickListener(this);
        findViewById(R.id.set_language).setOnClickListener(this);
        findViewById(R.id.get_cpu_temp).setOnClickListener(this);
        findViewById(R.id.set_adb_open).setOnClickListener(this);
        findViewById(R.id.set_adb_close).setOnClickListener(this);
        findViewById(R.id.replaceBootanimation).setOnClickListener(this);
        findViewById(R.id.openScreenAndVoice).setOnClickListener(this);
        findViewById(R.id.closeScreenAndVoice).setOnClickListener(this);
        findViewById(R.id.setStandByMode).setOnClickListener(this);
        findViewById(R.id.setNormalMode).setOnClickListener(this);
        findViewById(R.id.setDefaultLauncher).setOnClickListener(this);
        findViewById(R.id.poweronoff).setOnClickListener(this);
        findViewById(R.id.gpio).setOnClickListener(this);
        findViewById(R.id.get_sn_number).setOnClickListener(this);
        findViewById(R.id.get_vendor_id).setOnClickListener(this);
        findViewById(R.id.status_bar_status).setOnClickListener(this);
        findViewById(R.id.status_bar_hide_show).setOnClickListener(this);
        findViewById(R.id.get_dpi).setOnClickListener(this);
        findViewById(R.id.set_dpi).setOnClickListener(this);
        findViewById(R.id.get_home_screen_type).setOnClickListener(this);
        findViewById(R.id.get_the_secondary_screen_type).setOnClickListener(this);
        findViewById(R.id.start_app).setOnClickListener(this);
        findViewById(R.id.daemon).setOnClickListener(this);
        findViewById(R.id.set_network_adb_open).setOnClickListener(this);
        findViewById(R.id.set_network_adb_close).setOnClickListener(this);
        findViewById(R.id.app_install_whitelist).setOnClickListener(this);
        findViewById(R.id.app_install_blacklist).setOnClickListener(this);
        findViewById(R.id.rm_whitelist).setOnClickListener(this);
        findViewById(R.id.rm_blacklist).setOnClickListener(this);
//        if (VersionUtils.getAndroidModle().contains("rk3399")) {
            findViewById(R.id.change_secondary_screen_bright).setOnClickListener(this);
            findViewById(R.id.get_secondary_screen_bright).setOnClickListener(this);
            findViewById(R.id.change_secondary_screen_bright).setVisibility(View.VISIBLE);
            findViewById(R.id.get_secondary_screen_bright).setVisibility(View.VISIBLE);
//        }

//        findViewById(R.id.set_volume).setOnClickListener(this);
    }

    private BroadcastReceiver mountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.MEDIA_MOUNTED".equals(intent.getAction())) {
                String path = intent.getData().getPath();
                Log.d("chenhuan", "externalStoragePath=" + path);
                if (path.contains("usb"))
                    USBPath = path;
                else if (path.contains("other"))///storage/0000-0000
                    TFPath = path;
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_api_version:
                ToastUtils.showToast(this, manager.getApiVersion());
                break;
            case R.id.get_device_type:
                ToastUtils.showToast(this, manager.getAndroidModle());
                break;
            case R.id.get_android_version:
                ToastUtils.showToast(this, manager.getAndroidVersion());
                break;
            case R.id.get_firmware_size:
                ToastUtils.showToast(this, manager.getRunningMemory());
                break;
            case R.id.get_in_store_size:
                ToastUtils.showToast(this, manager.getInternalStorageMemory());
                break;
            case R.id.get_firmware_sdk_version:
                ToastUtils.showToast(this, manager.getFirmwareVersion());
                break;
            case R.id.get_firmware_kernel_version:
                ToastUtils.showToast(this, manager.getKernelVersion());
                break;
            case R.id.get_device_firmware_version:
                ToastUtils.showToast(this, manager.getAndroidDisplay());
                break;
            case R.id.get_firmware_date:
                ToastUtils.showToast(this, manager.getFirmwareDate());
//                MySDK.timingSwitcher(System.currentTimeMillis()+1000*60*5,System.currentTimeMillis()+1000*60*10,false);
                break;
            case R.id.get_cpu_type:
                ToastUtils.showToast(this, manager.getCPUType());
                break;
            case R.id.shutdown:
                manager.shutdown();
                break;
            case R.id.reboot:
                manager.reboot();
                break;
            case R.id.nav_bar_state:
                ToastUtils.showToast(this, "导航栏是否隐藏：" + manager.getNavBarHideState());
                break;
            case R.id.nav_bar_hide_show:
                manager.hideNavBar(!manager.getNavBarHideState());
                break;
            case R.id.nav_bar_slide_state:
                ToastUtils.showToast(this, "滑出导航栏是否打开：" + manager.isSlideShowNavBarOpen());
                break;
            case R.id.nav_bar_slide:
                manager.setSlideShowNavBar(!manager.isSlideShowNavBarOpen());
                break;
            case R.id.notification_bar_slide_state:
                ToastUtils.showToast(this, "滑出通知栏是否打开：" + manager.isSlideShowNotificationBarOpen());
                break;
            case R.id.notification_bar_slide:
                manager.setSlideShowNotificationBar(!manager.isSlideShowNotificationBarOpen());
                break;
            case R.id.screen_shot:
//                if (manager.takeScreenshot()!=null){
//                    File file = new File(Environment.getExternalStorageDirectory().getPath() +"/002.jpg");
//                    try {
//                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
//                        manager.takeScreenshot().compress(Bitmap.CompressFormat.JPEG,100,bos);
//                        bos.flush();
//                        bos.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    ToastUtils.showToast(this,"截图存储在 /mnt/sdcard/001.jpg");
//                }

                if (manager.takeScreenshot(Environment.getExternalStorageDirectory().getPath() + "/001.jpg"))
                    ToastUtils.showToast(this, "截图存储在 "+Environment.getExternalStorageDirectory().getPath() + "/001.jpg");
//                if (MySDK.screenshot(Environment.getExternalStorageDirectory().getPath() +"/001.jpg"))
//                    ToastUtils.showToast(this,"截图存储在 /mnt/sdcard/001.jpg");

//                File file = new File(Environment.getExternalStorageDirectory().getPath() +"/001.txt");
//                if (!file.exists()) {
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    GPIOUtils.writeStringFileFor7(file,"546546546546");
//                }
//                ToastUtils.showToast(this,GPIOUtils.readGpioPGForLong(Environment.getExternalStorageDirectory().getPath() +"/001.txt"));

                break;
            case R.id.vice_screen_shot:
                if (manager.viceScreenshot(Environment.getExternalStorageDirectory().getPath() + "/002.jpg"))
                    ToastUtils.showToast(this, "副屏截图存储在 "+Environment.getExternalStorageDirectory().getPath() + "/002.jpg");
                break;
            case R.id.get_width_height:
                int[] display = manager.getDisplayHeight_width(this);
                ToastUtils.showToast(this, "Width = " + display[0] + " height = " + display[1]);
                break;
            case R.id.get_secondary_screen_width_height:
                if (manager.getScreenResolution(1) == null)
                    ToastUtils.showToast(this, "无副屏");
                else
                    ToastUtils.showToast(this, "width==" + manager.getScreenResolution(1).getMode().getPhysicalWidth() +
                            "  hight ==" + manager.getScreenResolution(1).getMode().getPhysicalHeight());
                break;
            case R.id.change_screen_bright:
                i = i + 10;
                if (i > 100)
                    i = 10;
                manager.changeScreenLight(i);
                break;
            case R.id.open_screen_bright:
                manager.turnOnBackLight();
                break;
            case R.id.close_screen_bright:
                manager.turnOffBackLight();
                break;
            case R.id.is_screen_light_on:
                ToastUtils.showToast(this, "背光是否打开 = " + manager.isBacklightOn());
                break;
            case R.id.get_brightness:
                ToastUtils.showToast(this, "背光亮度 = " + manager.getSystemBrightness());
                break;
            case R.id.open_hdmi:
                manager.turnOnHDMI();
                break;
            case R.id.close_hdmi:
                manager.turnOffHDMI();
                break;
            case R.id.rotate_90:
                manager.rotateScreen(this, "90");
                break;
            case R.id.rotate_0:
                manager.rotateScreen(this, "0");
                break;
            case R.id.update_img:
                if (Build.VERSION.SDK_INT < 25) {
                    if (new File(Environment.getExternalStorageDirectory().getPath() + "/Download/update.img").exists())
                        manager.upgradeSystem(Environment.getExternalStorageDirectory().getPath() + "/update.img");
                    else
                        ToastUtils.showToast(this, "请在内置存储Download下放置img升级包");
                } else {
                    if (new File(Environment.getExternalStorageDirectory().getPath() + "/Download/update.zip").exists())
                        manager.upgradeSystem(Environment.getExternalStorageDirectory().getPath() + "/Download/update.zip");
                    else
                        ToastUtils.showToast(this, "请在内置存储Download下放置zip升级包");
                }
                Log.d("chenhuan", "externalStoragePath = " + Environment.getExternalStorageDirectory().getPath() + "/Download/update.zip");
                break;
            case R.id.update_img_show_dialog:
                manager.setUpdateSystemWithDialog(true);
                ToastUtils.showToast(this, "升级固件显示对话框");
                break;
            case R.id.update_img_not_show_dialog:
                manager.setUpdateSystemWithDialog(false);
                ToastUtils.showToast(this, "升级固件不显示对话框");
                break;
            case R.id.update_img_delete_file:
                manager.setUpdateSystemDelete(true);
                ToastUtils.showToast(this, "升级固件删除固件原文件");
                break;
            case R.id.update_img_not_delete_file:
                manager.setUpdateSystemDelete(false);
                ToastUtils.showToast(this, "升级固件不删除固件原文件");
                break;
            case R.id.recovery:
                manager.rebootRecovery();
                break;
            case R.id.silent_install:
                boolean isScuess = manager.silentInstallApk(Environment.getExternalStorageDirectory().getPath() + "/KeyTest.apk", true);
                Toast.makeText(this, "静默安装返回值" + isScuess, Toast.LENGTH_LONG).show();// /mnt/sdcard/Download
                Log.d("chenhuan","externalStoragePath = " + Environment.getExternalStorageDirectory().getPath() +"/test.apk");
//                manager.silentInstallApk("/mnt/sdcard/Download/sougoushurufa_831.apk");
                break;
            case R.id.silent_uninstall:
                manager.unInstallApk("rkandroidapi.ys.com.rkandroidapi");
//                MySDK.uninstallApplication("rkandroidapi.ys.com.rkandroidapi");
                break;
            case R.id.get_eth_status:
                ToastUtils.showToast(this, "以太网是否打开=" + manager.getEthStatus());
                break;
            case R.id.get_eth_mode:
                ToastUtils.showToast(this, "以太网模式为=" + manager.getEthMode());
                break;
            case R.id.get_eth_mac:
                ToastUtils.showToast(this, "Mac地址 = " + manager.getEthMacAddress());
                break;
            case R.id.set_eth_mac:
                Log.i("yuanhang", com.ys.rkapi.Utils.Utils.getValueFromProp("persist.sys.displayrot"));
                ToastUtils.showToast(this, "设置以太网mac地址为ee4e592090cf");
                break;
            case R.id.get_eth_ip:
                if (DHCP.equals(manager.getEthMode()))
                    ToastUtils.showToast(this, "动态IP地址：" + manager.getDhcpIpAddress());
                else if (STATICIP.equals(manager.getEthMode()))
                    ToastUtils.showToast(this, "静态IP地址：" + manager.getStaticEthIPAddress());
                break;
            case R.id.set_eth_ip:
                manager.setStaticEthIPAddress(IPADDR, GATEWAY, MASK, DNS1, DNS2);
                break;
            case R.id.open_eth:
                manager.ethEnabled(true);
                break;
            case R.id.close_eth:
                manager.ethEnabled(false);
                break;
            case R.id.set_dhcp_eth:
                manager.setDhcpIpAddress(this);
                break;
            case R.id.get_mask:
                ToastUtils.showToast(this, "以太网子网掩码 = " + manager.getNetMask());
                break;
            case R.id.get_gateway:
                ToastUtils.showToast(this, "以太网网关 = " + manager.getGateway());
                break;
            case R.id.get_dns1:
                ToastUtils.showToast(this, "dns1 = " + manager.getEthDns1());
                break;
            case R.id.get_dns2:
                ToastUtils.showToast(this, "dns2 = " + manager.getEthDns2());
                break;
//            case R.id.set_pppoe_dial:
//                manager.setPppoeDial(this, "ddddddg", "1111222");
//                break;
            case R.id.externel_sd_path:
                ToastUtils.showToast(this, "外置SD卡路径 = " + manager.getSDcardPath());
//                if ("".equals(TFPath) || TFPath == null)
//                    Toast.makeText(getApplicationContext(), "请插入TF卡", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "外置TF卡路径 = " + TFPath, Toast.LENGTH_SHORT).show();
                break;
            case R.id.externel_u_path:
                ToastUtils.showToast(this, "U 盘路径 = " + manager.getUSBStoragePath(0));
//                if ("".equals(USBPath) || USBPath == null)
//                    Toast.makeText(getApplicationContext(), "请插入U盘", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(getApplicationContext(), "外置U盘路径 = " + USBPath, Toast.LENGTH_SHORT).show();
                break;
            case R.id.uninstall_externel:
                String USBStorage = "/mnt/usb_storage/USB_DISK1";
                manager.unmountVolume(USBStorage);
                Toast.makeText(getApplicationContext(), "卸载" + USBStorage, Toast.LENGTH_SHORT).show();
                break;
            case R.id.mount_externel:
                String USBStorage1 = "/mnt/usb_storage/USB_DISK1";
                manager.mountVolume(USBStorage1);
                Toast.makeText(getApplicationContext(), "挂载" + USBStorage1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.get_uartPath:
                ToastUtils.showToast(this, "串口0的绝对路径 = " + manager.getUartPath("TTYS0"));
                break;
            case R.id.set_time:
                manager.switchAutoTime(false);
                manager.setTime(2017, 3, 16, 17, 44, 55);
                break;
            case R.id.su_order:
                ToastUtils.showToast(this, "是否有su:" + manager.execSuCmd());
                break;
            case R.id.net_type:
                ToastUtils.showToast(this, "上网类型 =" + manager.getCurrentNetType());
                break;
//            case R.id.screen_num:
//                ToastUtils.showToast(this,"屏幕数" + manager.getScreenNumber());
//                break;
            case R.id.hdmi:
                ToastUtils.showToast(this, "HDMI =" + manager.getHdmiinStatus());
//                ToastUtils.showToast(this,MySDK.isHDMI()+"");
                break;
            case R.id.switch_on_time:
                manager.switchAutoTime(true);
                ToastUtils.showToast(this, "打开自动确定日期和时间");
                break;
            case R.id.switch_off_time:
                manager.switchAutoTime(false);
                ToastUtils.showToast(this, "关闭自动确定日期和时间");
                break;
            case R.id.is_auto_time:
                ToastUtils.showToast(this, "是否打开自动更新时间开关 = " + manager.isAutoSyncTime());
                break;
            case R.id.open_softkeyboard:
                if ("27".equals(Build.VERSION.SDK))
                    ToastUtils.showToast(this, "暂未实现");
                manager.setSoftKeyboardHidden(false);
                break;
            case R.id.close_softkeyboard:
                if ("27".equals(Build.VERSION.SDK))
                    ToastUtils.showToast(this, "暂未实现");
                manager.setSoftKeyboardHidden(true);
                break;
            case R.id.set_dormant_interval:
                manager.setDormantInterval(this, 15000);//2147483647
                break;
//            case R.id.awaken:
//                Handler handler = new Handler();
//                handler.postDelayed(awakeRunnable,20000);
//                Toast.makeText(this,"20s后唤醒屏幕",Toast.LENGTH_LONG).show();
//                break;
            case R.id.set_default_inputmethod:
                ToastUtils.showToast(this, "是否成功设置默认输入法为谷歌拼音输入法:" +
                        manager.isSetDefaultInputMethodSuccess("com.google.android.inputmethod.pinyin/.PinyinIME"));//  com.xinshuru.inputmethod/.FTInputService com.sohu.inputmethod.sogou  com.google.android.inputmethod.pinyin
                break;
            case R.id.get_default_inputmethod:
                ToastUtils.showToast(this, "当前输入法是:" + manager.getDefaultInputMethod());
                break;
            case R.id.set_language:
                manager.setLanguage("en", "US");
                break;
            case R.id.get_kernel_log:
//                String path1 = Environment.getExternalStorageDirectory().getPath();
//                manager.getKmsgLog(path1 + "/kernelLog.txt");
                break;
            case R.id.get_android_log:
//                manager.getAndroidLogcat(externalStoragePath +"/androidLog.txt");
                break;
            case R.id.stop_android_log:
//                manager.stopAndroidLogcat();
                break;
            case R.id.get_cpu_temp:
                ToastUtils.showToast(this, "cpu的温度是" + manager.getCPUTemperature());
                break;
            case R.id.set_adb_open:
                manager.setADBOpen(true);
                break;
            case R.id.set_adb_close:
                manager.setADBOpen(false);
                break;
            case R.id.replaceBootanimation:
                manager.replaceBootanimation(externalStoragePath + "/bootanimation.zip");
                break;
            case R.id.setDefaultLauncher:
                manager.setDefaultLauncher("com.android.launcher3/com.android.launcher3.Launcher");
                break;
            case R.id.poweronoff:
                Intent intent = new Intent(MainActivity.this, PowerOnOffActivity.class);
                startActivity(intent);
                break;
            case R.id.gpio:
                Intent intent1 = new Intent(MainActivity.this, GpioActivity.class);
                startActivity(intent1);
                break;
            case R.id.get_sn_number:
                ToastUtils.showToast(this, "sn=" + manager.getSn());
                break;
            case R.id.get_vendor_id:
                ToastUtils.showToast(this, "厂商识别码=" + manager.getVendorID());
                break;
            case R.id.status_bar_status:
                ToastUtils.showToast(this, "状态栏是否隐藏:" + manager.getStatusBar());
                break;
            case R.id.status_bar_hide_show:
                manager.hideStatusBar(manager.getStatusBar());
                break;
            case R.id.get_dpi:
                ToastUtils.showToast(this, "dpi为:" + manager.getDpi());
                break;
            case R.id.set_dpi:
                manager.setDpi(1);
                break;
            case R.id.get_home_screen_type:
                ToastUtils.showToast(this, "主屏幕类型为:" + manager.getHomeScreenType());
                break;
            case R.id.get_the_secondary_screen_type:
                ToastUtils.showToast(this, "副屏幕类型为:" + manager.getSecondaryScreenType());
                break;
            case R.id.start_app:
                manager.selfStart("com.bjw.ComAssistant");
                break;
            case R.id.daemon:
                manager.daemon("com.bjw.ComAssistant", 1);
                break;
            case R.id.set_network_adb_open:
                manager.setNetworkAdb(true);
                break;
            case R.id.set_network_adb_close:
                manager.setNetworkAdb(false);
                break;
            case R.id.app_install_whitelist:
                manager.setAppInstallWhitelist("true", "com.bjw.ComAssistant");
                break;
            case R.id.app_install_blacklist:
                manager.setAppInstallBlacklist("true", "com.bjw.ComAssistant");
                break;
            case R.id.get_secondary_screen_bright:
                ToastUtils.showToast(this, "副屏亮度为:" + manager.getSecondBackLight());
                break;
            case R.id.change_secondary_screen_bright:
                j = j + 10;
                if (j > 100)
                    j = 10;
                manager.changeSecondBackLight(j);
                break;
            case R.id.rm_whitelist:
                manager.removeFileData("com.bjw.ComAssistant",false);
                break;
            case R.id.rm_blacklist:
                manager.removeFileData("com.bjw.ComAssistant",true);
                break;
            default:
                break;
        }
    }

    //    public void setCurrentTimeMillis(long time) {
//        try {
//                TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
//                Date current = new Date(time);
//                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd.HHmmss");
//                String datetime = df.format(current);
//                Process process = Runtime.getRuntime().exec("su");
//                DataOutputStream os = new DataOutputStream(process.getOutputStream());
//                //os.writeBytes("setprop persist.sys.timezone GMT\n");
//                os.writeBytes("/system/bin/date -s " + datetime + "\n");
//                os.writeBytes("clock -w\n");
//                os.writeBytes("exit\n");
//                os.flush();
//                return true;
//        } catch (Exception e) {
//        }
//    }
    private Runnable awakeRunnable = new Runnable() {
        @Override
        public void run() {
            manager.awaken();
            Log.d("sky", "awaken");
        }
    };

    @Override
    protected void onDestroy() {
        manager.unBindAIDLService(this);
        unregisterReceiver(mountReceiver);
        super.onDestroy();
    }
}
