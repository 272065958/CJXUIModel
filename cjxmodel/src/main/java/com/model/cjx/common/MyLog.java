package com.model.cjx.common;

import android.util.Log;

import com.model.cjx.BuildConfig;

/**
 * create by cjx on 2018/12/17
 */
public class MyLog {
    public static void E(String msg){
        if(BuildConfig.DEBUG){
            Log.e("cjx", msg);
        }
    }
}
