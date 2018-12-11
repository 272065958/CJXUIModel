package com.model.cjx.common

import android.text.TextUtils
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * Created by cjx on 18-1-9.
 * json和对象的转换工具, 用Gson插件
 */
object JsonParser {
    private val gson = Gson()

    /**
     * 将json字符串转成对象
     */
    fun <T> fromJson(json: String, typeOfT: Type?): T? {
        return try {
            gson.fromJson(json, typeOfT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取指定key的json字符串并转换成对象
     *
     * 此处的(typeOfT)不能直接在函数里面写成(object : TypeToken<T>() {}.type), 否则不能正常解析
     */
    fun <T> fromJson(key: String, json: JSONObject, typeOfT: Type?): T? {
        if (json.has(key) && typeOfT != null) {
            val value: String? = try {
                json.getString(key)
            } catch (e: JSONException) {
                e.printStackTrace()
                null
            }
            if (TextUtils.isEmpty(value)) {
                return null
            }

            return fromJson(value!!, typeOfT)
        } else {
            return null
        }
    }

    /**
     * 将对象转成json字符串
     */
    fun toJson(any: Any): String = gson.toJson(any)
}