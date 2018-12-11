package com.model.cjx.base.fragment

import android.content.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.Toolbar
import android.view.*
import com.model.cjx.MyApplication
import com.model.cjx.R
import com.model.cjx.base.activity.BaseActivity

/**
 * Created by cjx on 17-11-28.
 * fragment 基类
 */
abstract class BaseFragment : Fragment() {

    /**
     * 需要回调的fragment
     */
    private var resultFragment: BaseFragment? = null
    /**
     * 请求返回值的类型
     */
    private var requestCode: Int = 0
    /**
     * fragment的view
     */
    private var contentView: View? = null
    /**
     * 当前activity
     */
    var baseActivity: BaseActivity? = null
    /**
     * 默认广播监听者
     */
    private var broadcastReceiver: BroadcastReceiver? = null
    /**
     * 是否含有标题栏
     */
    protected var hasTitleBar = false

    protected var toolbar: Toolbar? = null
    /**
     * 与activity绑定
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        baseActivity = if (activity is BaseActivity) {
            activity as BaseActivity
        } else {
            throw ExceptionInInitializerError("is must extends BaseActivity")
        }
    }

    /**
     * 创建fragment view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (contentView == null) {
            contentView = createView(inflater, container)
//            contentView!!.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.cjx_background_color))
        }

        return contentView
    }

    override fun onDetach() {
        super.onDetach()
        baseActivity = null
    }

    protected abstract fun createView(inflater: LayoutInflater, container: ViewGroup?): View

    /**
     * 注册默认广播监听者
     */
    protected fun registerReceiver(filter: IntentFilter) {
        if (broadcastReceiver == null) {
            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    onBroadcastReceive(intent)
                }
            }
            activity?.registerReceiver(broadcastReceiver, filter)
        } else {
            throw SecurityException("the broadcastreceiver has register, use another?")
        }

    }

    /**
     * 注销默认广播
     */
    protected open fun unregisterReceiver() {
        if (broadcastReceiver != null) {
            activity?.unregisterReceiver(broadcastReceiver)
            broadcastReceiver = null
        }
    }

    /**
     * 默认广播回调
     */
    protected open fun onBroadcastReceive(intent: Intent?) = Unit

    /**
     * 设置标题栏
     */
    protected open fun setToolbar(title: String?, showBack: Boolean = true, backListener: View.OnClickListener? = null) {
        toolbar = contentView?.findViewById<Toolbar>(R.id.toolbar)
        if (toolbar == null) {
            View.inflate(context, MyApplication.instance.getToolbarLayout(), contentView as? ViewGroup)
            toolbar = contentView?.findViewById<Toolbar>(R.id.toolbar) ?: return
        }
        val bar = toolbar!!
        hasTitleBar = true
        val app = MyApplication.instance
        bar.setBackgroundResource(app.getToolbarBg())
        bar.setTitleTextAppearance(context, R.style.toolbar_title)
        bar.title = title
        if (showBack) {
            bar.setNavigationIcon(app.getBackRes())
            baseActivity?.setSupportActionBar(bar)
            if (backListener == null) {
                bar.setNavigationOnClickListener { finish() }
            } else {
                bar.setNavigationOnClickListener(backListener)
            }
        } else {
            baseActivity?.setSupportActionBar(bar)
        }
    }

    /**
     * 创建menu
     */
    protected open fun createMenu(menu: Menu?, menuId: Int) {
        toolbar?.menu?.clear()
        toolbar?.inflateMenu(menuId)
    }

    /**
     * 更改标题
     */
    protected open fun setToolbarTitle(title: String) {
        val toolbar = contentView?.findViewById<Toolbar>(R.id.toolbar) ?: return
        toolbar.title = title
    }

    /**
     * 显示消息提示
     */
    protected open fun showToast(str: String) = baseActivity?.showToast(str)

    /**
     * 替换对应id的fragment
     */
    protected open fun resetFragment(id: Int, fragment: Fragment) {
        baseActivity?.resetFragment(id, fragment)
    }

    /**
     * 切换一个fragment
     */
    protected open fun startFragment(id: Int, fragment: Fragment, backStack: String? = null, hideFragment: Fragment? = activity?.supportFragmentManager?.findFragmentById(id)) {
        if (backStack != null) {
            baseActivity?.startFragment(id, fragment, hideFragment, backStack)
        } else {
            baseActivity?.startFragment(id, fragment, hideFragment)
        }
    }

    /**
     * 切换一个fragment
     */
    protected open fun startFragmentWithoutAnim(id: Int, fragment: Fragment, backStack: String? = null, hideFragment: Fragment? = activity?.supportFragmentManager?.findFragmentById(id)) {
        if (backStack != null) {
            baseActivity?.startFragmentWithoutAnim(id, fragment, hideFragment, backStack)
        } else {
            baseActivity?.startFragmentWithoutAnim(id, fragment, hideFragment)
        }
    }

    /**
     * 在当前容器内切换一个fragment
     */
    protected open fun startCustomFragment(id: Int, tagFragment: Fragment, hideFragment: Fragment? = null, withAnim: Boolean = true, backStack: String? = this.toString()) {
        val beginTransaction = childFragmentManager.beginTransaction()
        if (withAnim) {
            beginTransaction.setCustomAnimations(
                    R.anim.open_slide_in, R.anim.open_slide_out, R.anim.close_slide_in, R.anim.close_slide_out
            )
        }
        if (hideFragment != null) {
            beginTransaction.hide(hideFragment)
        }
        if (tagFragment.isAdded) {
            beginTransaction.show(tagFragment)
        } else {
            beginTransaction.add(id, tagFragment)
        }
        if (backStack != null) {
            beginTransaction.addToBackStack(backStack)
        }
        beginTransaction.commit()
    }

    /**
     * 切换其父fragment指定ip的fragment
     */
    protected open fun startCustomFragmentFromParent(id: Int, tagFragment: Fragment, hideFragment: Fragment? = this, withAnim: Boolean = true, backStack: String? = this.toString()) {
        (parentFragment as BaseFragment).startCustomFragment(id, tagFragment, hideFragment, withAnim, backStack)
    }

    /**
     * 显示加载对话框
     */
    open fun showLoadDialog(listener: DialogInterface.OnCancelListener? = null) = baseActivity?.showLoadDialog(listener)

    /**
     * 隐藏加载对话框
     */
    open fun dismissLoadDialog() {
        baseActivity?.dismissLoadDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        resultFragment = null
        unregisterReceiver()
    }

    /**
     * 实现类似Activity的startActivityForResult的功能
     */
    protected open fun startFragmentForResult(id: Int, fragment: BaseFragment, requestCode: Int, backStack: String? = null) {
        fragment.resultFragment = this
        fragment.requestCode = requestCode
        startFragment(id, fragment, backStack)
    }

    /**
     * 实现类似Activity的setResult的功能
     */
    protected open fun setResult(bundle: Bundle?) {
        resultFragment?.onResultReceive(requestCode, bundle)
    }

    /**
     * 关闭当前fragment
     */
    protected open fun finish(name: String? = null) {
        if (name != null) {
            activity?.supportFragmentManager?.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        } else {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    /**
     * fragment间的回传回调
     */
    open fun onResultReceive(requestCode: Int, bundle: Bundle?) = Unit

}