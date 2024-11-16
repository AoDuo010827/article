package com.ys.myapi;

interface IGpio {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    String getGpioValue(int index);
    boolean setGpioValue(int index,String arg);
    String getGpioDirection(int index);
    boolean setGpioDirection(int index,int arg);

}

