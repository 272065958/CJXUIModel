package com.model.cjx.component

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View

/**
 * Created by cjx on 18-3-19.
 * 表格布局的recyclerView的分隔线
 */

class RecyclerViewGridDivider(private val dividerHeight: Int = 2, dividerColor: Int = Color.parseColor("#d8d8d8")) : RecyclerView.ItemDecoration() {

    private val mPaint: Paint

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = dividerColor
        mPaint.style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
//        val position = parent.getChildAdapterPosition(view)
//        val spanCount = getSpanCount(parent)
//        val itemCount = parent.adapter?.itemCount
        // 如果是最后一行, 不绘制底部

        // 如果是最后一列, 不绘制右边
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager ?: return -1
        return when (layoutManager) {
            is GridLayoutManager -> layoutManager.spanCount
            is StaggeredGridLayoutManager -> layoutManager.spanCount
            else -> -1
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        drawRect(c, parent)
    }

    private fun drawRect(c: Canvas?, parent: RecyclerView?) {
        val count = parent?.childCount ?: 0
        for (i in 0 until count) {
            val view = parent?.getChildAt(i) ?: continue
            val lp = view.layoutParams as RecyclerView.LayoutParams

            // draw horizontal
            val leftH = view.left - lp.leftMargin
            val rightH = view.right + lp.rightMargin
            val topH = view.bottom + lp.bottomMargin
            val bottomH = topH + dividerHeight
            c?.drawRect(leftH.toFloat(), topH.toFloat(), rightH.toFloat(), bottomH.toFloat(), mPaint)

            // draw vertical
            val leftV = view.right + lp.rightMargin
            val rightV = leftV + dividerHeight
            val topV = view.top - lp.topMargin
            val bottomV = view.bottom + lp.bottomMargin
            c?.drawRect(leftV.toFloat(), topV.toFloat(), rightV.toFloat(), bottomV.toFloat(), mPaint)
        }
    }
}