package com.model.cjx.http

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * Created by cjx on 18-3-14.
 * 响应类, 监听响应进度
 */

class ProgressResponseBody(private val responseBody: ResponseBody, private val progressListener: ProgressResponseListener) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long = responseBody.contentLength()

    override fun contentType(): MediaType? = responseBody.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            //当前读取字节数
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                //增加当前读取的字节数，如果读取完成了bytesRead会返回-1
                totalBytesRead = if (bytesRead != -1L) {
                    totalBytesRead + bytesRead
                } else {
                    totalBytesRead
                }
                //回调，如果contentLength()不知道长度，会返回-1
                progressListener.onResponseProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }

    interface ProgressResponseListener {
        fun onResponseProgress(bytesResd: Long, longContentLength: Long, done: Boolean)
    }
}