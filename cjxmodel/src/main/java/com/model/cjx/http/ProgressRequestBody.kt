package com.model.cjx.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

/**
 * Created by cjx on 18-3-14.
 * 请求类, 监听响应进度
 */
class ProgressRequestBody(private val requestBody: RequestBody, private val progressListener: ProgressRequestListener) : RequestBody() {

    private var bufferedSink: BufferedSink? = null

    /**
     * 重写调用实际的响应体的contentType
     * @return MediaType
     */
    override fun contentType(): MediaType? = requestBody.contentType()

    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     */
    override fun contentLength(): Long = requestBody.contentLength()

    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink))
        }
        try {
            requestBody.writeTo(bufferedSink!!)
            bufferedSink!!.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private fun sink(sink: Sink): Sink = object : ForwardingSink(sink) {
        //当前写入字节数
        private var bytesWritten = 0L
        //总字节长度，避免多次调用contentLength()方法
        private var contentLength = 0L

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (contentLength == 0L) {
                //获得contentLength的值，后续不再调用
                contentLength = contentLength()
            }
            //增加当前写入的字节数
            bytesWritten += byteCount
            progressListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength)
        }
    }

    interface ProgressRequestListener {
        fun onRequestProgress(bytesWritten: Long, longContentLength: Long, done: Boolean)
    }
}