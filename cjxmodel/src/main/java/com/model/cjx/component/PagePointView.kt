package com.model.cjx.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by cjx on 18-3-16.
 * 显示翻页位置的点
 */

class PagePointView(context: Context, attributeSet: AttributeSet? = null) : View(context, attributeSet) {

    private var selectXOff = 0f
    private var xOffUnit = 0f

    private var count: Int = 0
    private var pointSize: Int = 0
    private var pointSpace: Int = 0

    private var unSelectPaint: Paint? = null
    private var selectPaint: Paint? = null

    /**
     * 初始化总页数和, 和标志的大小和素材
     */
    fun initPoint(pointSize: Int, pointSpace: Int, count: Int, selectColor: Int, unSelectColor: Int) {
        val unit = pointSize + pointSpace
        val width = count * unit - pointSpace
        xOffUnit = unit.toFloat()
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(width + 6, pointSize + 6)
        } else {
            layoutParams.height = pointSize + 6
            layoutParams.width = width + count + 6
        }

        this.count = count
        this.pointSize = pointSize
        this.pointSpace = pointSpace

        if (unSelectPaint != null) {
            return
        }
        unSelectPaint = Paint()
        unSelectPaint!!.style = Paint.Style.STROKE
        unSelectPaint!!.strokeWidth = 1f
        unSelectPaint!!.color = unSelectColor
        unSelectPaint!!.isAntiAlias = true

        selectPaint = Paint()
        selectPaint!!.style = Paint.Style.FILL
        selectPaint!!.color = selectColor
        selectPaint!!.isAntiAlias = true
    }

    /**
     * 设置当前位置
     */
    fun setPosition(position: Int) {
        val xOff = position * xOffUnit + pointSize / 2f + 2f
        if(selectXOff != xOff){
            selectXOff = xOff
        }
        invalidate()
    }

    /**
     * 重画界面
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0 until count) {
            canvas.drawCircle(pointSize / 2f + i * (pointSize + pointSpace) + 2f,
                    pointSize / 2f + 2f,
                    pointSize / 2f, unSelectPaint!!)
        }
        canvas.drawCircle(selectXOff, pointSize / 2f + 2f, pointSize / 2f - 1f, selectPaint!!)
    }
}