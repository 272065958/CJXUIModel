package com.model.cjx.dialog

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.model.cjx.MyApplication
import com.model.cjx.R

/**
 * Created by cjx on 18-1-9.
 * 加载提示对话框
 */
class LoadDialog(context: Context, tip: String? = null) : Dialog(context, R.style.loading_dialog) {

    private var tipView: TextView? = null

    init {
        setContentView(R.layout.dialog_load)
        setCanceledOnTouchOutside(false)

        if (tip != null) {
            setTip(tip)
        }
    }

    fun setTip(tip: String?) {
        if (tipView == null) {
            tipView = TextView(context)
            tipView?.setTextColor(MyApplication.instance.getColorPrimary())
            val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.topMargin = context.resources.getDimensionPixelOffset(R.dimen.text_margin)
            findViewById<ViewGroup>(R.id.dialog_load).addView(tipView, lp)
        }
        if (TextUtils.isEmpty(tip)) {
            tipView?.visibility = View.GONE
        } else {
            tipView?.visibility = View.VISIBLE
            tipView!!.text = tip
        }
    }
}