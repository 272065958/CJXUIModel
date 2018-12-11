package com.model.cjx.base.fragment

import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.model.cjx.R
import com.model.cjx.base.adapter.BaseRecyclerAdapter
import com.model.cjx.component.FooterLoadRecyclerView
import com.model.cjx.component.RecyclerViewLinearDivider
import java.util.*

/**
 * Created by cjx on 18-3-12.
 * 加载列表模板
 */

abstract class BaseRecyclerFragment<T, in E> : BaseLoadFragment<E>() {
    /**
     * 加载下一页的开关, 最好在onCreate时设置
     */
    open var openLoadMore = true
    /**
     * 列表适配器
     */
    var adapter: BaseRecyclerAdapter<T>? = null
    /**
     * 列表控件
     */
    var recyclerView: FooterLoadRecyclerView? = null
    /**
     * 加载下一页回调
     */
    private var footerLoadListener: FooterLoadRecyclerView.FooterLoadListener? = null
    /**
     * 加载下一页的view
     */
    private var loadNextView: View? = null

    /**
     * 当前页码和每页数量
     */
    open var page = 1
    open var limit = 14

    /**
     * 加载完成后初始化界面控件
     */
    override fun createContentView(): View {
        recyclerView = FooterLoadRecyclerView(activity!!)
        return recyclerView!!
    }

    /**
     * 刷新数据时, 要重置当前页码
     */
    override fun startLoad() {
        if (openLoadMore) {
            page = 1
        }
        super.startLoad()
    }

    /**
     * 结束加载时, 需要判断是否要隐藏加载下一个的view
     */
    override fun endLoad() {
        if (openLoadMore && loadNextView?.visibility == View.VISIBLE) {
            loadNextView!!.visibility = View.GONE
        }
        super.endLoad()
    }

    /**
     * 加载完显示列表数据
     */
    open fun displayList(list: ArrayList<T>?, emptyTitle: String = "没有找到数据", emptyIcon: Int = 0, isLastPage: Boolean = false, checkEmpty: Boolean = true) {
        // 当前数据是否为null
        if (checkEmpty) {
            if (list == null || list.isEmpty()) {
                // 如果当前页码是1, 需要显示空界面
                if (page == 1) {
                    showEmptyView(emptyTitle, emptyIcon)
                }
                if (openLoadMore && recyclerView != null) {
                    recyclerView!!.footerLoadListener = null
                    recyclerView!!.footerLoadState = false
                }
                return
            } else {
                // 隐藏空界面
                hideEmptyView()
            }
        }
        // 初始化显示数据的contentView
        initContentView()
        val recyclerView = this.recyclerView!!
        if (adapter == null) {
            // 初始化adapter
            adapter = getMyBaseAdapter(list)
            // 设置分割线
            setLayoutManagerAndDivider(recyclerView)
            recyclerView.adapter = adapter
        } else {
            if (!openLoadMore || page == 1) {
                // 没有加载下一页功能 或者 当前加载的是第一页
                adapter!!.notifyDataSetChanged(list)
            } else {
                // 将当前页数据加载到已有数据上
                val oldList = adapter!!.list
                oldList?.addAll(list!!)
                adapter!!.notifyDataSetChanged(oldList)
                val manager = recyclerView.layoutManager
                if (manager is LinearLayoutManager && manager.orientation == LinearLayoutManager.HORIZONTAL) {
                    recyclerView.smoothScrollBy(resources.getDimensionPixelSize(R.dimen.fab_margin), 0)
                } else {
                    recyclerView.smoothScrollBy(0, resources.getDimensionPixelSize(R.dimen.fab_margin))
                }
            }
        }
        // 添加加载下一页的功能
        if (openLoadMore) {
            if (page == 1) {
                // 滚回顶部
                recyclerView.layoutManager?.scrollToPosition(0)
            }
            if (isLastPage) {
                recyclerView.footerLoadListener = null
            } else {

                recyclerView.footerLoadListener = getFooterLoadListener()
            }
            recyclerView.footerLoadState = false
        } else {
            // 滚回顶部
            recyclerView.layoutManager?.scrollToPosition(0)
        }
    }

    /**
     * 初始化加载下一页的提示view
     */
    private fun initLoadNextView() {
        loadNextView = View.inflate(activity, R.layout.load_more_view, null)

        val lp = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.BOTTOM
        loadNextView!!.layoutParams = lp
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadNextView!!.elevation = resources.getDimension(R.dimen.elevation_bottom_bar)
        }
    }

    /**
     * 获取加载下一个的回调监听
     */
    private fun getFooterLoadListener(): FooterLoadRecyclerView.FooterLoadListener {
        if (footerLoadListener == null) {
            footerLoadListener = object : FooterLoadRecyclerView.FooterLoadListener {
                override fun loadMore(view: RecyclerView) {
                    // 添加下拉刷新组件
                    if (loadNextView == null) {
                        initLoadNextView()
                        parentView.addView(loadNextView)
                    } else {
                        loadNextView!!.visibility = View.VISIBLE
                    }
                    page++
                    loadData()
                }
            }
        }
        return footerLoadListener!!
    }

    /**
     * 设置recyclerview的排版和分割线
     */
    open fun setLayoutManagerAndDivider(recyclerView: RecyclerView) {
        val manager = LinearLayoutManager(activity)
        manager.orientation = LinearLayoutManager.VERTICAL
        val decoration = getItemDecoration(LinearLayoutManager.VERTICAL)
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration)
        }
        recyclerView.layoutManager = manager
    }

    /**
     * 当前列表的分隔线
     */
    open fun getItemDecoration(orientation: Int): RecyclerView.ItemDecoration? = RecyclerViewLinearDivider(orientation)

    /**
     * 获取当前列表的适配器
     */
    abstract fun getMyBaseAdapter(list: ArrayList<T>?): BaseRecyclerAdapter<T>
}