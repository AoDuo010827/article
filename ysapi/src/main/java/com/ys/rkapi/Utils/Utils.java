package com.ys.rkapi.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.ys.rkapi.Constant;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/12/14.
 */

public class Utils {
    //白名单地址
    private static String whitePath = "data/app/whiteListApps.txt";
    //黑名单地址
    private static String blackPath = "data/app/blackListApps.txt";

    public static void setValueToProp(String key, String val) {
        Class<?> classType;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method method = classType.getDeclaredMethod("set", new Class[]{String.class, String.class});
            method.invoke(classType, new Object[]{key, val});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValueFromProp(String key) {
        String value = "";
        try {
            Class<?> classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", new Class<?>[]{String.class});
            value = (String) getMethod.invoke(classType, new Object[]{key});
        } catch (Exception e) {
        }
//        Log.i("yuanhang",value);
        return value;
    }


    public static void do_exec(String cmd) {
        try {
            /* Missing read/write permission, trying to chmod the file */
            Process su;
            su = Runtime.getRuntime().exec("su");
            String str = cmd + "\n" + "exit\n";
            su.getOutputStream().write(str.getBytes());

            if ((su.waitFor() != 0)) {
                System.out.println("cmd=" + cmd + " error!");
                throw new SecurityException();
            }
        } catch (Exception e) {
            Log.d("chenhuan","e = " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean execFor7AndReboot(String command) {
        Log.d("execFor7","command = " + command);
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String s = command + "\n";
            dataOutputStream.write(s.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("execFor7", "execFor7 msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("execFor7", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }

        execFor7("reboot");
        return result;
    }


    public static boolean execFor7(String command) {
        Log.d("execFor7","command = " + command);
        boolean result = false;
        DataOutputStream dataOutputStream = null;
        BufferedReader errorStream = null;
        try {
            // 申请su权限
            Process process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            // 执行pm install命令
            String s = command + "\n";
            dataOutputStream.write(s.getBytes(Charset.forName("utf-8")));
            dataOutputStream.flush();
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            process.waitFor();
            errorStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String msg = "";
            String line;
            // 读取命令的执行结果
            while ((line = errorStream.readLine()) != null) {
                msg += line;
            }
            Log.d("execFor7", "execFor7 msg is " + msg);
            // 如果执行结果中包含Failure字样就认为是安装失败，否则就认为安装成功
            if (!msg.contains("Failure")) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("execFor7", e.getMessage(), e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                Log.e("TAG", e.getMessage(), e);
            }
        }
        return result;
    }

    public static void reboot() {
        execFor7("reboot");
    }
    public static void reboot(Context mContext) {
        if (mContext != null) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.reboot");
            if (Integer.parseInt(Build.VERSION.SDK) > 25)
                intent.setPackage(Constant.YSRECEIVER_PACKAGE_NAME);
            mContext.sendBroadcast(intent);
        }
    }

    public static String getEthernet(Context context) {
        String value = "";
        try {
            Class<?> classType = Class.forName("android.net.EthernetManager");
            Method getMethod = classType.getDeclaredMethod("getIpAddress");
            @SuppressLint("WrongConstant") Object obj = (context.getSystemService("ethernet"));
            value = (String) getMethod.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 图片按比例大小压缩方法
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 这里压缩50%，把压缩后的数据存放到baos中
//        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        Log.i("Utils", w + "---------------" + h);
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
         float hh = 800f;// 这里设置高度为800f
         float ww = 480f;// 这里设置宽度为480f
//        float hh = 512f;
//        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
         newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
        //return bitmap;
    }

    public static void hideBar() {
        Class<?> classType;
        try {
            classType = Class.forName("com.android.internal.statusbar.IStatusBarService$Stub");
            Method asInterface = classType.getDeclaredMethod("asInterface", IBinder.class);
            Object IStatusBarService = asInterface.invoke(null,getService("statusbar"));
            Method hideNavigationBar = IStatusBarService.getClass().getDeclaredMethod("hideBar", new Class[0]);
            hideNavigationBar.invoke(IStatusBarService,new Object[]{});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideNavigationBar() {
        Class<?> classType;
        try {
            classType = Class.forName("com.android.internal.statusbar.IStatusBarService$Stub");
            Method asInterface = classType.getDeclaredMethod("asInterface", IBinder.class);
            Object IStatusBarService = asInterface.invoke(null,getService("statusbar"));
            Method hideNavigationBar = IStatusBarService.getClass().getDeclaredMethod("hideNavigationBar",new Class[0]);
            hideNavigationBar.invoke(IStatusBarService,new Object[]{});
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static IBinder getService(String name){
        IBinder iBinder = null;
        try{
            Class<?> clz = Class.forName("android.os.ServiceManager");
            Method checkService = clz.getMethod("getService", String.class);
            iBinder = (IBinder)checkService.invoke(clz.newInstance(), name);
        }
        catch(Exception e) {}
        return iBinder;
    }

    public static boolean isRoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 增加指定名单
     * @param packageName
     */
    public static void writeFileData(String packageName,boolean black) {
        FileWriter fw = null;
        String path = "";
        if (black)
            path = blackPath;
        else
            path = whitePath;
        File file = new File(path);
        try {
            if (!file.exists()) file.createNewFile();
            fw = new FileWriter(file,true);
            //向文件中写内容  加上换行
            fw.write(packageName + "\n");
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除指定名单
     * @param packageName
     */
    public static void removeFileData(String packageName,boolean black) {
        File file = null;
        FileWriter fw = null;
        String path = "";
        if (black)
            path = blackPath;
        else
            path = whitePath;
        file = new File(path);
        try {
            if (!file.exists()) file.createNewFile();
            String result = readFileData(black);
            String[] ss = result.split("\\n");
            fw = new FileWriter(file);
            //清空之前写的白名单
            for(String s:ss){
                if(!s.trim().equals(packageName)) {
                    //向文件中写内容  加上换行
                    fw.write(s + "\n");
                }
            }
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取所有的名单
     * @return
     */
    public static String readFileData(boolean black){
        String path = "";
        if (black)
            path = blackPath;
        else
            path = whitePath;
        File file = new File(path);
        if(!file.exists())return "";
        StringBuilder result = new StringBuilder();
        try{
            String s = "";
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            while((s = br.readLine())!=null){
                //使用readLine方法，一次读一行
                //result.append(System.lineSeparator()+s);
                result.append(s + "\n");
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.e("result",result.toString());
        return result.toString();
    }

    /**
     * 清除所有名单
     */
    public static void clearData(boolean black){
        FileWriter fw = null;
        String path = "";
        if (black)
            path = blackPath;
        else
            path = whitePath;
        File file = new File(path);
        try {
            if (!file.exists()) file.createNewFile();
            fw = new FileWriter(file);
            //向文件中写内容  加上换行
            fw.write("" );
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readGpioPG(String path) {
        String str = "";
        File file = new File(path);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[16];
            fileInputStream.read(buffer);
            fileInputStream.close();
            str = new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void writeStringFileFor7(File file, String content) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
