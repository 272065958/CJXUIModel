package com.model.cjx.common

import android.content.Context
import android.os.Environment
import android.webkit.URLUtil
import java.io.File
import java.util.regex.Pattern

/**
 * Created by cjx on 18-1-8.
 */
object Tools {
    private var tempPath: String? = null
    private var cachePath: String? = null

    /**
     * 创建文件
     */
    fun newFile(filePath: String): File {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        return file
    }

    /**
     * 创建当前目录的文件夹
     */
    fun mkDir(filePath: String?) {
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 获取程序缓存的cache路径
     */
    fun getCachePath(context: Context): String {
        if (cachePath == null) {
            cachePath = getDiskCacheDir(context) + "/"
        }
        mkDir(cachePath)
        return cachePath!!
    }

    /**
     * 获取程序缓存的cache路径
     */
    fun getTempPath(context: Context): String {
        if (tempPath == null) {
            tempPath = getCachePath(context) + "temp/"
        }
        mkDir(tempPath)
        return tempPath!!
    }


    /**
     * 获取程序缓存路径
     */
    fun getDiskCacheDir(context: Context): String? {
        val hasExternal = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
        val cacheDir: File? = if (hasExternal) {
            context.externalCacheDir
        } else {
            context.cacheDir
        }
        return cacheDir?.absolutePath
    }

    /**
     * 判断是否手机格式
     */
    fun isPhone(mobile: String): Boolean = Pattern.compile("1\\d{10}").matcher(mobile).matches()

    /**
     * 判断是否url格式
     */
    fun isUrl(url: String): Boolean = URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)
}