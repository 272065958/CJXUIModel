package com.model.cjx.component

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.model.cjx.R

/**
 * Created by cjx on 2018/5/9.
 * 空界面控件
 */
class EmptyView : LinearLayout {

    var iconView: ImageView? = null
    var clickTipView: TextView? = null
    var titleView: TextView

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        titleView = TextView(context)
        titleView.setTextColor(ContextCompat.getColor(context, R.color.cjx_text_main_color))
        addView(titleView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        val padding = resources.getDimensionPixelOffset(R.dimen.double_fab_margin)
        setPadding(padding, padding, padding, padding)
    }

    /**
     * 设置界面显示数据
     */
    fun setEmptyData(emptyTitle: String? = "没有数据...", emptyIcon: Int = -1) {
        titleView.text = emptyTitle
        if (emptyIcon > 0) {
            if (iconView == null) {
                iconView = ImageView(context)
                val size = resources.getDimensionPixelSize(R.dimen.empty_icon)
                val lp = LayoutParams(size, size)
                addView(iconView, 0, lp)
            } else if (iconView!!.visibility == View.GONE) {
                iconView!!.visibility = View.VISIBLE
            }
            iconView!!.setImageResource(emptyIcon)
        } else if (iconView != null) {
            iconView!!.setImageDrawable(null)
            iconView!!.visibility = View.GONE
        }
    }

    /**
     * 设置点击事件
     */
    fun setClickListener(l: OnClickListener?, tip: String = "可以点我刷新 (｡･ω･｡)") {
        if (clickTipView == null) {
            clickTipView = TextView(context)
            clickTipView!!.setTextColor(ContextCompat.getColor(context, R.color.cjx_text_main_color))
            val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            lp.topMargin = resources.getDimensionPixelOffset(R.dimen.text_margin)
            addView(clickTipView, lp)
        }
        clickTipView!!.text = tip
        super.setOnClickListener(l)
    }
}