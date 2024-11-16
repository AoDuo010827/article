package rkandroidapi.ys.com.rkandroidapi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ToastUtils {
    private static Toast toast;

    /**
     * 解决Toast重复弹出 长时间不消失的问题 
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message){
//        if (toast==null){
//            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
//        }else {
//            toast.setText(message);
//        }
//        toast.show();//设置新的消息提示
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    int i = 0;
//                    File file = new File("/sys/class/gpio/gpio226/value");
//                    if (!file.exists())
//                    return;
//                    while (true) {
//                        writeNode("/sys/class/gpio/gpio226/value", "1");
//                        Thread.sleep(3000);
//                        if (i > 6)
//                            break;
//                    }
//                    writeNode("/sys/class/gpio/gpio226/value", "0");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
     }
    private static boolean writeNode(String path, String arg) {
        Log.d("GPIOUtils", "Gpio_test set node path: " + path + " arg: " + arg);
        if (path != null && arg != null) {
            FileWriter fileWriter = null;
            Object bufferedWriter = null;

            boolean var5;
            try {
                fileWriter = new FileWriter(path);
                fileWriter.write(arg);
                return true;
            } catch (Exception var15) {
                Log.e("GPIOUtils", "Gpio_test write node error!! path" + path + " arg: " + arg);
                var15.printStackTrace();
                var5 = false;
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }

                    if (bufferedWriter != null) {
                        ((BufferedWriter)bufferedWriter).close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }

            return var5;
        } else {
            Log.e("GPIOUtils", "set node error");
            return false;
        }
    }
} 