package com.model.cjx.dialog

import android.app.Dialog
import android.content.Context
import android.util.SparseArray
import android.widget.Toast
import com.model.cjx.R
import com.model.cjx.base.activity.BaseActivity

/**
 * Created by cjx on 2018/5/11.
 * 可以设置标记的对话框
 */
open class TagDialog(context: Context) : Dialog(context, R.style.loading_dialog) {
    var tag: Any? = null
    var tags: SparseArray<Any>? = null

    fun setTag(key: Int, tag: Any?) {
        if (tags == null) {
            tags = SparseArray()
        }
        tags!!.put(key, tag)
    }

    fun getTag(key: Int): Any? = tags?.get(key)

    fun showToast(message: String) =
            if (context is BaseActivity) {
                (context as BaseActivity).showToast(message)
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
}