package com.hss01248.aop_bytex_demo;

import android.util.Log;

import androidx.annotation.Keep;

/**
 * @Despciption todo
 * @Author hss
 * @Date 04/08/2022 14:21
 * @Version 1.0
 */
@Keep
public class Test implements Runnable{

    public static void test1(){
        Log.w("run","original test");
    }

    @Override
    public void run() {
        Log.w("run","original run");
    }
}
