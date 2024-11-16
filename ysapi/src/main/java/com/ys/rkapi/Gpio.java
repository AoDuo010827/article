package com.ys.rkapi;

import android.os.RemoteException;

import com.ys.myapi.IGpio;


public class Gpio {
    private IGpio mIGpio;
    public void setGpio(IGpio gpio){
        mIGpio = gpio;
    }

    public String getGpioValue(int index){
        if (mIGpio!=null){
            try {
                return mIGpio.getGpioValue(index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getGpioDirection(int index){
        if (mIGpio!=null){
            try {
                return mIGpio.getGpioDirection(index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean setGpioValue(int index,String arg){
        if (mIGpio!=null){
            try {
                return mIGpio.setGpioValue(index,arg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean setGpioDirection(int index,int arg){
        if (mIGpio!=null){
            try {
                return mIGpio.setGpioDirection(index,arg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
