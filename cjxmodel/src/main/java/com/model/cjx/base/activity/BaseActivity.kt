package com.model.cjx.base.activity

import android.content.*
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.model.cjx.MyApplication
import com.model.cjx.R
import com.model.cjx.dialog.LoadDialog
import java.lang.Exception
import java.util.*

/**
 * Created by cjx on 18-3-9.
 * activity 基类, 提供设置标题栏, 加载对话框, 消息提示, 显示/隐藏键盘, fragment操作等功能
 */

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 提交数据的提示对话框
     */
    var loadDialog: LoadDialog? = null
    /**
     * 显示软键盘时, 要一点点的时间延迟, 这个是延迟的计时器
     */
    private var timer: Timer? = null
    /**
     * 默认的广播接收者
     */
    private var broadcastReceiver: BroadcastReceiver? = null

    override fun setContentView(view: View?) {
        setStatusBar()
        // 设置沉浸式属性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            view?.fitsSystemWindows = true
        }
        super.setContentView(view)
    }

    override fun setContentView(layoutResID: Int) {
        setStatusBar()
        super.setContentView(layoutResID)
    }

    override fun onDestroy() {
        // 释放显示小键盘的计时器
        timer?.cancel()
        timer = null
        // 注销默认的广播
        unRegisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    /**
     * 设置顶部状态栏主题
     */
    private fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = MyApplication.instance.getStatusBarColor()
        }
    }

    /**
     * 设置toolbar
     *
     * @param showBack     是否显示返回按钮
     * @param backListener 返回按钮监听
     * @param titleRes     标题的资源id
     */
    protected open fun setToolBar(titleRes: String?, showBack: Boolean = true, backListener: View.OnClickListener? = null) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar) ?: return
        val app = MyApplication.instance
        toolbar.setBackgroundResource(app.getToolbarBg())
        toolbar.title = titleRes
        // 设置返回按钮
        if (showBack) {
            toolbar.setNavigationIcon(app.getBackRes())
            setSupportActionBar(toolbar)
            if (backListener == null) {
                toolbar.setNavigationOnClickListener { onBackPressed() }
            } else {
                toolbar.setNavigationOnClickListener(backListener)
            }
        } else {
            setSupportActionBar(toolbar)
        }
    }

    /**
     * 设置toolbar的title
     *
     * @param title 标题
     */
    open fun setToolbarTitle(title: String) {
        findViewById<Toolbar>(R.id.toolbar)?.title = title
    }

    /**
     * 获取标题
     */
    open fun getToolbarTitle(): String = findViewById<Toolbar>(R.id.toolbar)?.title.toString()

    /**
     * 显示加载对话框
     */
    fun showLoadDialog(listener: DialogInterface.OnCancelListener? = null) {
        if (loadDialog == null) {
            loadDialog = LoadDialog(this)
        } else if ((loadDialog!!.context as? BaseActivity)?.isFinishing == true) { // 可能当前fragment的上下文已经被销毁
            loadDialog = LoadDialog(this)
        }
        if (listener != null) {
            loadDialog!!.setOnCancelListener(listener)
        }
        try {
            loadDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 隐藏加载对话框
     */
    fun dismissLoadDialog() {
        if (loadDialog != null && loadDialog!!.isShowing) {
            loadDialog!!.dismiss()
        }
    }

    /**
     * 显示键盘
     */
    fun showInput() {
        if (timer == null) {
            timer = Timer()
        }
        timer?.schedule(object : TimerTask() {
            override fun run() = getInputMethodManager().toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }, 200)
    }

    /**
     * 隐藏小键盘
     */
    fun hideInput() {
        getInputMethodManager().hideSoftInputFromWindow(currentFocus?.windowToken ?: return, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * 返回软键盘管理器
     */
    private fun getInputMethodManager(): InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


    /**
     * 显示提示信息
     */
    open fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * 注册默认广播
     */
    protected fun registerReceiver(filter: IntentFilter) {
        if (broadcastReceiver != null) {
            throw SecurityException("the broadcastreceiver has register, use another?")
        }
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) = onBroadcastReceiver(intent)
        }
        registerReceiver(broadcastReceiver, filter)
    }

    /**
     * 默认广播接收信息回调
     */
    open fun onBroadcastReceiver(intent: Intent?) = Unit

    /**
     * 注销默认广播
     */
    open fun unRegisterReceiver(receiver: BroadcastReceiver?) {
        if (receiver != null) {
            unregisterReceiver(receiver)
        }
    }

    /**
     * 开启一个fragment界面, 不带动画效果
     */
    open fun startFragmentWithoutAnim(id: Int, tagFragment: Fragment, hideFragment: Fragment? = null, backStack: String? = null) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction(beginTransaction, id, tagFragment, hideFragment, backStack)
    }

    /**
     * 开启一个fragment界面
     */
    open fun startFragment(id: Int, tagFragment: Fragment, hideFragment: Fragment? = null, backStack: String? = null) {
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.setCustomAnimations(
                R.anim.open_slide_in, R.anim.open_slide_out, R.anim.close_slide_in, R.anim.close_slide_out
        )
        fragmentTransaction(beginTransaction, id, tagFragment, hideFragment, backStack)
    }

    /**
     * 提交切换fragment的事物
     * hideFragment为null时, 可能在下次切换的时候可以看到当前fragment的view, 因为切换动画是透明渐变
     */
    private fun fragmentTransaction(beginTransaction: FragmentTransaction?, id: Int, tagFragment: Fragment, hideFragment: Fragment?, backStack: String? = null) {
        if (hideFragment != null) {
            beginTransaction?.hide(hideFragment)
        }
        if (tagFragment.isAdded) {
            beginTransaction?.show(tagFragment)
        } else {
            beginTransaction?.add(id, tagFragment)
        }
        if (backStack != null) {
            // 类似startActivity
            beginTransaction?.addToBackStack(backStack)
        }

        beginTransaction?.commit()
    }

    /**
     * 重置指定id上的fragment
     */
    fun resetFragment(id: Int, fragment: Fragment) = supportFragmentManager.beginTransaction().replace(id, fragment).commit()
}
