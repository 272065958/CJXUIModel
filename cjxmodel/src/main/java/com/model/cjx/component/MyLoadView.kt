package com.model.cjx.component

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.widget.ProgressBar
import com.model.cjx.MyApplication

@SuppressLint("ViewConstructor")
/**
 * Created by cjx on 18-1-9.
 * 旋转进度提示界面
 */
class MyLoadView : ProgressBar {
    constructor(context: Context?) : super(context) {
        val field = ProgressBar::class.java.getDeclaredField("mDuration")
        field.isAccessible = true
        field.setInt(this, 900)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    /**
     * 适配android版本的加载view
     */
    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            indeterminateTintList = ColorStateList.valueOf(MyApplication.instance.getColorPrimaryDark())
            indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
        } else {
            indeterminateDrawable = MyApplication.instance.getLoadDrawable()
        }
    }
}