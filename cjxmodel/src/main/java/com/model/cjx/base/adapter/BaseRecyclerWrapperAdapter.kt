package com.model.cjx.base.adapter

import android.content.Context
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup

/**
 * Created by cjx on 17-12-14.
 * 给recyclerview添加headerview和footerview
 */
abstract class BaseRecyclerWrapperAdapter<T>(list: ArrayList<T>?, context: Context) : BaseRecyclerAdapter<T>(list, context) {

    private val mHeaderViews: SparseArrayCompat<View> = SparseArrayCompat()
    private val mFooterViews: SparseArrayCompat<View> = SparseArrayCompat()

    open fun isHeaderViewPosition(position: Int): Boolean = position < mHeaderViews.size()
    open fun isFooterViewPosition(position: Int): Boolean = position >= mHeaderViews.size() + count
    open fun addHeaderView(view: View) = mHeaderViews.put(mHeaderViews.size() + HEADER_TYPE, view)
    open fun addFooterView(view: View) = mFooterViews.put(mFooterViews.size() + FOOTER_TYPE, view)

    override fun getItemCount(): Int = super.getItemCount() + mHeaderViews.size() + mFooterViews.size()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when {
        mHeaderViews.get(viewType) != null -> WrapperViewHolder(mHeaderViews.get(viewType)!!)
        mFooterViews.get(viewType) != null -> WrapperViewHolder(mFooterViews.get(viewType)!!)
        else -> bindViewHolder(createView(parent, inflater, viewType), viewType)
    }

    override fun getItemViewType(position: Int): Int = when {
        isHeaderViewPosition(position) -> HEADER_TYPE + position
        isFooterViewPosition(position) -> FOOTER_TYPE + position - mHeaderViews.size() - count
        else -> -1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when {
        isHeaderViewPosition(position) -> Unit
        isFooterViewPosition(position) -> Unit
        else -> wrapperBingViewHolder(holder, position - mHeaderViews.size())
    }

    /**
     * 适应grid布局的头尾适配器
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = getItemViewType(position)
                    return when {
                        mHeaderViews.get(viewType) != null -> layoutManager.spanCount
                        mFooterViews.get(viewType) != null -> layoutManager.spanCount
                        else -> 1
                    }
                }
            }
            layoutManager.spanCount = layoutManager.spanCount
        }
    }

    /**
     * 适应staggeredGrid布局的头尾适配器
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.layoutPosition
        if (isHeaderViewPosition(position) || isFooterViewPosition(position)) {
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                lp.isFullSpan = true
            }
        }
    }

    /**
     * header的个数
     */
    fun getHeaderCount(): Int = mHeaderViews.size()

    /**
     * footer的个数
     */
    fun getFooterCount(): Int = mFooterViews.size()

    /**
     * 绑定数据到itemview上
     */
    abstract fun wrapperBingViewHolder(holder: RecyclerView.ViewHolder?, position: Int)

    companion object {
        const val HEADER_TYPE: Int = 199000
        const val FOOTER_TYPE: Int = 200000
    }

    class WrapperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}