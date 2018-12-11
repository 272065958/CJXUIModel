package com.model.cjx.component

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by cjx on 18-1-9.
 * recyclerview 的分隔线
 */
class RecyclerViewLinearDivider(private val orientation: Int, private val dividerHeight: Int = 2, dividerColor: Int = Color.parseColor("#d8d8d8"))
    : RecyclerView.ItemDecoration() {
    private val mPaint: Paint

    init {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw IllegalArgumentException("不支持线性布局以外的布局样式")
        }
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = dividerColor
        mPaint.style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        if(position != 0){
            outRect.set(0, dividerHeight, 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            LinearLayoutManager.HORIZONTAL -> {
                drawVertical(c, parent)
            }
            LinearLayoutManager.VERTICAL -> {
                drawHorizontal(c, parent)
            }
        }
    }

    private fun drawVertical(c: Canvas?, parent: RecyclerView?) {
        parent ?: return
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + layoutParams.rightMargin
            val right = left + dividerHeight
            c?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }

    private fun drawHorizontal(c: Canvas?, parent: RecyclerView?) {
        parent ?: return
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val layoutParams = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + layoutParams.bottomMargin
            val bottom = top + dividerHeight
            c?.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
        }
    }
}