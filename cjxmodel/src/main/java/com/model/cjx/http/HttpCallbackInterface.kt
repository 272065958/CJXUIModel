package com.model.cjx.http

import java.lang.reflect.Type

/**
 * Created by cjx on 18-3-13.
 * 网络数据加载回调
 */
interface HttpCallbackInterface<in T> {
    fun showError(error: String, icon: Int = -1)
    fun tokenError(error: String)
    fun setContentData(obj: T?, vararg tag: Any?)
    fun getType(): Type?
}