package com.model.cjx.common;

import android.content.Context;
import android.os.Environment;
import android.webkit.URLUtil;

import java.io.File;
import java.util.regex.Pattern;

/**
 * create by cjx on 2018/12/17
 */
public class Tools {

    private static Tools instance;

    private Tools() {

    }

    public static Tools getInstance() {
        if (instance == null) {
            synchronized (Tools.class) {
                if (instance == null) {
                    instance = new Tools();
                }
            }
        }
        return instance;
    }

    private String tempPath = null;
    private String cachePath = null;

    /**
     * 创建文件
     */
    public File newFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    /**
     * 创建当前目录的文件夹
     */
    public void mkDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 获取程序缓存的cache路径
     */
    public String getCachePath(Context context) {
        if (cachePath == null) {
            cachePath = getDiskCacheDir(context) + "/";
        }
        mkDir(cachePath);
        return cachePath;
    }

    /**
     * 获取程序缓存的cache路径
     */
    public String getTempPath(Context context) {
        if (tempPath == null) {
            tempPath = getCachePath(context) + "temp/";
        }
        mkDir(tempPath);
        return tempPath;
    }


    /**
     * 获取程序缓存路径
     */
    public String getDiskCacheDir(Context context) {
        boolean hasExternal = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        File cacheDir = hasExternal ? context.getExternalCacheDir() : context.getCacheDir();
        return cacheDir.getAbsolutePath();
    }

    /**
     * 判断是否手机格式
     */
    public boolean isPhone(String mobile) {
        return Pattern.compile("1\\d{10}").matcher(mobile).matches();
    }

    /**
     * 判断是否url格式
     */
    public boolean isUrl(String url) {
        return URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url);
    }
}
