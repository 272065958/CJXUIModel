package com.model.cjx.http

import android.os.Handler
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by cjx on 2018/4/17.
 * 下载工具
 */
class DownloadUtil(val downloadHandler: Handler, val tempFile: File, url: String) {

    var downloadCall: Call?

    init {
        downloadCall = HttpUtils.getInstance().download(url, getProgressResponse(), getCallback())
    }

    /**
     * 获取一个response请求进度监听
     */
    private fun getProgressResponse(): ProgressResponseBody.ProgressResponseListener = object : ProgressResponseBody.ProgressResponseListener {
        override fun onResponseProgress(bytesResd: Long, longContentLength: Long, done: Boolean) {
            if (longContentLength != -1L) {
                val p = (100 * bytesResd / longContentLength).toInt()
                if (p == 100 && done) {
                    // 下载成功, 置空call
                    downloadCall = null
                    downloadHandler.sendEmptyMessageDelayed(DOWNLOAD_SUCCESS, 200)
                } else {
                    // 发送下载进度
                    downloadHandler.sendEmptyMessage(p)
                }
            }
        }
    }

    private fun getCallback(): Callback = object: Callback {
        override fun onFailure(call: Call?, e: IOException?) {
            downloadFail()
        }

        override fun onResponse(call: Call?, response: Response?) {
            if (response != null) {
                // 删除旧文件
                if (tempFile.exists()) {
                    tempFile.delete()
                }
                if (tempFile.createNewFile()) {
                    val inputStream = response.body()!!.byteStream()
                    val fos = FileOutputStream(tempFile)
                    val buf = ByteArray(1024)
                    var hasRead = inputStream.read(buf)
                    while (hasRead != -1){
                        fos.write(buf, 0, hasRead)
                        fos.flush()
                        hasRead = inputStream.read(buf)
                    }
                    fos.close()
                    inputStream.close()
                } else {
                    downloadCall = null
                    downloadHandler.sendEmptyMessage(DOWNLOAD_FAIL_FILE)
                }
            } else {
                downloadFail()
            }
        }
    }

    /**
     * 下载失败
     */
    private fun downloadFail() {
        downloadCall = null
        downloadHandler.sendEmptyMessage(DOWNLOAD_FAIL)
    }

    /**
     * 取消下载
     */
    fun cancel() {
        val call = downloadCall ?: return
        if (call.isExecuted) {
            call.cancel()
            downloadCall = null
        }
    }

    companion object {
        val DOWNLOAD_FAIL_FILE = 202
        val DOWNLOAD_FAIL = 201
        val DOWNLOAD_SUCCESS = 200
    }
}