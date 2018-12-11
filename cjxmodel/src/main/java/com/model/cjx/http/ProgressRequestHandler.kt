package com.model.cjx.http

import android.os.Handler

/**
 * Created by cjx on 18-3-14.
 * 请求处理监听, 监听请求进度
 */

class ProgressRequestHandler(private val handler: Handler) : ProgressRequestBody.ProgressRequestListener {

    companion object {
        val UPLOAD_SUCCESS = 200
    }

    private var progress = 0

    override fun onRequestProgress(bytesWritten: Long, longContentLength: Long, done: Boolean) {
        if (longContentLength != -1L) {
            // 长度未知的情况下回返回-1
            val p = ((100 * bytesWritten) / longContentLength).toInt()
            if (progress != p) {
                progress = p
                handler.sendEmptyMessage(progress)
            }
            if(progress == 100 && done){
                handler.sendEmptyMessage(UPLOAD_SUCCESS)
            }
        }
    }
}