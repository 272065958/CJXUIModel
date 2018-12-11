package com.model.cjx.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.model.cjx.R

/**
 * Created by cjx on 2018/5/11.
 * 自定义从底部弹出的对话框
 */
open class SlideDialog(context: Context) : TagDialog(context) {
    override fun setContentView(view: View) {
        super.setContentView(view)
        setDialogStyle()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setDialogStyle()
    }

    private fun setDialogStyle() {
        window!!.setWindowAnimations(R.style.slide_dialog)
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}