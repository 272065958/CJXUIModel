package com.model.cjx.common

import android.util.Log

import com.model.cjx.MyApplication

/**
 * Created by cjx on 17-8-7.
 * 打印日志, release版本不打印
 */
object MyLog {

    fun e(msg: String, tag: String = "cjx") {
        if (MyApplication.instance.debug) {
            Log.e(tag, msg)
        }
    }

    fun e(msg: String){
        if (MyApplication.instance.debug) {
            Log.e("cjx", msg)
        }
    }
}
