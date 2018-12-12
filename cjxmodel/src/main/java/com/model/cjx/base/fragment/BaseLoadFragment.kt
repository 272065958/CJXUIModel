package com.model.cjx.base.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.model.cjx.MyApplication
import com.model.cjx.R
import com.model.cjx.component.EmptyView
import com.model.cjx.component.MyLoadView

/**
 * Created by cjx on 18-3-12.
 * 基本加载数据碎片
 */
abstract class BaseLoadFragment : BaseFragment() {

    /**
     * 标记是否调用过loadData函数
     */
    private var hasLoaded = false

    /**
     * 标记是否初始化界面
     */
    private var isInitContent = false
    /**
     * 当前是否加载中的状态
     */
    private var isLoading = false
    /**
     * 是否打开刷新功能, 最好在onCreate时设置
     */
    protected var openRefresh = true

    /**
     * 当前fragment显示的view, 根据不同情况, 显示LoadView或者EmpytView或者ContentView
     */
    protected lateinit var parentView: ViewGroup
    /**
     * 显示数据的view
     */
    private var contentView: View? = null
    /**
     * 下拉刷新控件
     */
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    /**
     * 加载数据的view
     */
    private var loadView: MyLoadView? = null
    /**
     * 空数据时显示的view
     */
    private var emptyView: EmptyView? = null

    /**
     * 创建启动时展示给用户的加载视图
     */
    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        parentView = createParentView(inflater)
        return parentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (loadView == null) {
            loadView = MyLoadView(activity)
            loadView!!.layoutParams = getCenterLayoutParams()
            parentView.addView(loadView)
        }
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return when {
            hasLoaded -> {
                super.onCreateAnimation(transit, enter, nextAnim)
            }
            nextAnim > 0 -> {
                val anim = AnimationUtils.loadAnimation(context, nextAnim)
                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) = Unit

                    override fun onAnimationEnd(animation: Animation?) {
                        startLoad()
                        hasLoaded = true
                    }

                    override fun onAnimationStart(animation: Animation?) = Unit
                })
                anim
            }
            else -> {
                startLoad()
                hasLoaded = true
                null
            }
        }
    }

    open fun createParentView(inflater: LayoutInflater): ViewGroup = FrameLayout(activity!!)

    /**
     * 加载网络数据, 第一次初始化和下拉刷新时会被触发
     */
    open fun startLoad() {
        isLoading = true
        loadData()
    }

    /**
     * 重新加载
     */
    fun reloadData() {
        showLoadView()
        startLoad()
    }

    /**
     * 显示加载进度框
     */
    fun showLoadView() {
        if (loadView != null && loadView!!.parent == null) {
            // 移除内容相关界面
            hideEmptyView()
            if (swipeRefreshLayout != null) {
                parentView.removeView(swipeRefreshLayout)
            } else if (contentView != null) {
                parentView.removeView(contentView)
            }
            parentView.addView(loadView)
        }
    }

    /**
     * 初始化刷新控件, 添加到父容器上
     */
    open fun initRefreshView() {
        swipeRefreshLayout = SwipeRefreshLayout(activity!!)
        swipeRefreshLayout!!.setColorSchemeColors(MyApplication.instance.getColorPrimaryDark())

        swipeRefreshLayout!!.setOnRefreshListener {
            if (isLoading) {
                swipeRefreshLayout!!.isRefreshing = false
            } else {
                startLoad()
            }
        }
    }

    /**
     * 移除错误界面
     */
    protected fun hideEmptyView() {
        if (emptyView != null && emptyView!!.parent != null) {
            parentView.removeView(emptyView)
        }
    }

    /**
     * 创建一个错误视图
     */
    open fun showEmptyView(emptyTitle: String?, emptyIcon: Int = 0) {
        if (openRefresh) { // 这里如果用visibility控制, 当再显示swipeRefreshLayout时, 有一瞬会显示上次未影藏的下拉刷新加载框
            parentView.removeView(swipeRefreshLayout)
        } else {
            parentView.removeView(contentView)
        }

        if (emptyView == null) {
            emptyView = EmptyView(context)
            emptyView!!.setClickListener(View.OnClickListener {
                hideEmptyView()
                if (loadView != null) {
                    parentView.addView(loadView)
                }
                startLoad()
            })
            emptyView!!.layoutParams = getCenterLayoutParams()
        }
        if (emptyView!!.parent == null) {
            parentView.addView(emptyView!!)
        }
        emptyView!!.setEmptyData(emptyTitle, emptyIcon)
    }

    /**
     * 获取默认居中的布局
     */
    open fun getCenterLayoutParams(width: Int = ViewGroup.LayoutParams.WRAP_CONTENT, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT): FrameLayout.LayoutParams {
        val lp = FrameLayout.LayoutParams(width, height)
        lp.gravity = Gravity.CENTER
        if (hasTitleBar) {
            lp.topMargin = resources.getDimensionPixelOffset(R.dimen.toolbar_height) / 2
        }
        return lp
    }

    /**
     * 获取全屏view的布局参数
     */
    open fun getFullLayoutParams(): FrameLayout.LayoutParams {
        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        if (hasTitleBar) {
            lp.topMargin = resources.getDimensionPixelOffset(R.dimen.toolbar_height)
        }
        return lp
    }

    /**
     * 结束加载状态
     */
    open fun endLoad() {
        isLoading = false
        if (swipeRefreshLayout != null && swipeRefreshLayout!!.isRefreshing) {
            swipeRefreshLayout!!.isRefreshing = false
        } else if (loadView?.parent != null) {
            parentView.removeView(loadView)
        }
    }

    /**
     * 初始化加载内容后的布局, 通常在获取数据完成,并且有数据的时候调用
     */
    open fun displayContentView() {
        // 如果初始化view, 则重新添加view到parentView上, 如果没有, 创建contentView
        if (!isInitContent) {
            isInitContent = true
            if (openRefresh) {
                initRefreshView()
                contentView = createContentView()
                swipeRefreshLayout!!.addView(contentView, getFullLayoutParams())
                parentView.addView(swipeRefreshLayout)
            } else {
                contentView = createContentView()
                parentView.addView(contentView, getFullLayoutParams())
            }
        } else {
            if (openRefresh) {
                if (swipeRefreshLayout?.parent == null) {
                    parentView.addView(swipeRefreshLayout)
                }
            } else {
                if (contentView?.parent == null) {
                    parentView.addView(contentView)
                }
            }
        }
    }

    /**
     * 初始化要显示的内容视图
     */
    abstract fun createContentView(): View

    /**
     * 加载网络数据
     */
    abstract fun loadData()

}