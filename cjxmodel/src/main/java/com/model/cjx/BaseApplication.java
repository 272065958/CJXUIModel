package com.model.cjx;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

/**
 * create by cjx on 2018/12/13
 */
public class BaseApplication extends Application {
    private int screenWidth = 0;

    public static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取屏幕宽度
     */
    public int getScreenWidth() {
        if (screenWidth == 0) {
            measureScreen();
        }
        return screenWidth;
    }

    /**
     * 获取返回按钮图标
     *
     * @return 资源id
     */
    public int getBackRes() {
        return R.drawable.white_back;
    }

    /**
     * 返回栏背景色
     *
     * @return 颜色
     */
    public int getToolbarBg() {
        return ContextCompat.getColor(this, R.color.cjx_colorPrimary);
    }

    /**
     * 程序主色调
     *
     * @return 颜色
     */
    public int getColorPrimary() {
        return ContextCompat.getColor(this, R.color.cjx_colorPrimary);
    }

    /**
     * 程序深色主色调
     *
     * @return 颜色
     */
    public int getColorPrimaryDark() {
        return ContextCompat.getColor(this, R.color.cjx_colorPrimaryDark);
    }

    /**
     * 程序着重色调
     *
     * @return 颜色
     */
    public int getColorAccrent() {
        return ContextCompat.getColor(this, R.color.cjx_colorAccent);
    }

    /**
     * 获取加载框
     *
     * @return 加载框图像
     */
    public Drawable getLoadDrawable() {
        return ContextCompat.getDrawable(this, R.drawable.loading);
    }

    /**
     * 获取状态栏颜色
     *
     * @return 颜色
     */
    public int getStatusBarColor() {
        return Color.BLACK;
    }

    /**
     * 获取标题栏布局
     *
     * @return 布局资源id
     */
    public int getToolbarLayout() {
        return R.layout.toolbar_view;
    }

    private void measureScreen() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenWidth = metrics.widthPixels;
    }
}
