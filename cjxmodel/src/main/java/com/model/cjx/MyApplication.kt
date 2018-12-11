package com.model.cjx

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

/**
 * Created by cjx on 17-12-28.
 * 程序基类
 */
abstract class MyApplication : Application() {

    private var SCREEN_WIDTH: Int = 0
    private var SCREEN_HEIGHT: Int = 0
    var debug: Boolean = true
    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
//        //拦截catch事件,自定义上传维护
//        val catchHandler = CrashHandler.getInstance()
//        catchHandler.init(this)
        instance = this
        debug = isDebug()
    }

    /**
     * 获取界面宽度
     */
    open fun getScreenWidth(): Int {
        if (SCREEN_WIDTH == 0) {
            measureScreen()
        }
        return SCREEN_WIDTH
    }

    /**
     * 获取界面高度
     */
    open fun getScreenHeight(): Int {
        if (SCREEN_HEIGHT == 0) {
            measureScreen()
        }
        return SCREEN_HEIGHT
    }

    private fun measureScreen() {
        val displayMetrics = resources.displayMetrics
        SCREEN_WIDTH = displayMetrics.widthPixels
        SCREEN_HEIGHT = displayMetrics.heightPixels
    }

    /**
     * 可以通过子类重写自定义
     * @return 默认返回按钮图标
     */
    open fun getBackRes(): Int = R.drawable.white_back

    /**
     * 可以通过子类重写自定义
     * @return 默认返回栏背景色
     */
    open fun getToolbarBg(): Int = R.color.cjx_colorPrimary

    /**
     * 可以通过子类重写自定义
     * @return 默认程序主色调
     */
    open fun getColorPrimary(): Int = ContextCompat.getColor(this, R.color.cjx_colorPrimary)


    /**
     * 可以通过子类重写自定义
     * @return 默认程序主色深色色调
     */
    open fun getColorPrimaryDark(): Int = ContextCompat.getColor(this, R.color.cjx_colorPrimaryDark)

    /**
     * 可以通过子类重写自定义
     * @return 默认程序按钮颜色
     */
    open fun getColorAccrent(): Int = ContextCompat.getColor(this, R.color.cjx_colorAccent)

    /**
     * 获取加载框
     */
    open fun getLoadDrawable(): Drawable? = ContextCompat.getDrawable(this, R.drawable.loading)

    /**
     * 获取状态栏颜色
     */
    open fun getStatusBarColor(): Int = Color.BLACK

    /**
     * 获取标题栏布局
     */
    open fun getToolbarLayout(): Int = R.layout.toolbar_view

    /**
     * 当前是否在开发环境
     */
    fun isDebug(): Boolean = debug

    /**
     * 设置用户信息
     */
    abstract fun setUser(data: Any?)

    /**
     * 跳转登录界面
     */
    abstract fun startLogin(activity: Activity)

    /**
     * 当前是否登录
     */
    abstract fun isLogin(): Boolean


}