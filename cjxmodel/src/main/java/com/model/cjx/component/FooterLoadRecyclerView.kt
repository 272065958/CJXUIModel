package com.model.cjx.component

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

/**
 * Created by cjx on 18-1-9.
 */
class FooterLoadRecyclerView(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {
    var footerLoadListener: FooterLoadListener? = null
    var footerLoadState = false

    init {

        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == SCROLL_STATE_IDLE && !footerLoadState && footerLoadListener != null && isSlideToBottom()) {
                    footerLoadState = true
                    footerLoadListener?.loadMore(this@FooterLoadRecyclerView)
                }
            }
        })
    }

    /**
     * 判断是否滚动到底部
     */
    fun isSlideToBottom(): Boolean = this.computeHorizontalScrollExtent() + this.computeHorizontalScrollOffset() >= this.computeHorizontalScrollRange() &&
            this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset() >= this.computeVerticalScrollRange()

    interface FooterLoadListener {
        fun loadMore(view: RecyclerView)
    }
}