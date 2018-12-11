package com.model.cjx.http

import android.text.TextUtils
import com.model.cjx.common.MyLog

import java.io.File
import java.io.InputStream
import java.util.ArrayList
import java.util.concurrent.TimeUnit

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.security.*
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * Created by cjx on 2016/6/21.
 * 访问网络的类
 */
class HttpUtils private constructor(supportHttps: Boolean = false, val stream: InputStream? = null) {

    private val client: OkHttpClient
    private val cookieJar: MyCookieJar

    // 默认添加在请求参数里面的值
    private var autoKeyValues: ArrayList<KeyValue>? = null

    private var MEDIA_TYPE_PNG: MediaType? = null

    private inner class MyCookieJar : CookieJar {
        private val cookieStore = ArrayList<Cookie>()

        override fun saveFromResponse(httpUrl: HttpUrl, list: List<Cookie>) {
            //当获得一个Response时，会调用这个方法来存储Cookie
            cookieStore.addAll(list)
        }

        override fun loadForRequest(httpUrl: HttpUrl): List<Cookie> {
            //当要call一个Request时，会调用这个方法来为请求的head添加cookie
            return cookieStore
        }

        fun clearCookie() {
            cookieStore.clear()
        }
    }

    class KeyValue(val key: String, val value: String)

    init {
        val builder: OkHttpClient.Builder = if (supportHttps) {
            getSSLHttpBuilder()
        } else {
            OkHttpClient.Builder()
        }
        cookieJar = MyCookieJar()
        client = builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .cookieJar(cookieJar).build()
    }

    /**
     * 清除登录cookid
     */
    fun clearCookie() {
        cookieJar.clearCookie()
    }

    /**
     * 如果有设置token的key， 每个接口会自动添加token
     */
    fun setAutoKeyValue(keyValues: ArrayList<KeyValue>): HttpUtils {
        this.autoKeyValues = keyValues
        return this
    }

    // 发起post请求
    fun postEnqueue(callback: Callback, url: String, vararg params: String?): Call {
        return postEnqueue(callback, null, MEDIATYPE_TEXT, url, *params)
    }

    // 发起带自定义header的post请求
    fun postEnqueue(callback: Callback, headers: ArrayList<KeyValue>?, contentType: Int, url: String, vararg params: String?): Call {
        MyLog.e(url)
        val request = getRequest(url,
                // TODO
                if (contentType == MEDIATYPE_TEXT) {
//                    getFormBody(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")!!, *params)
                    getFormBody(*params)
                } else {
                    getJsonFormBody(*params)
                }, headers)
        return enqueue(callback, request)
    }

    /**
     * 发起请求
     *
     * @param callback 回调接口
     * @param request  请求数据
     * @return 返回一个请求, 可以供用户取消
     */
    fun enqueue(callback: Callback, request: Request): Call {
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

    // 获取一个request
    private fun getRequest(url: String, body: RequestBody, headers: ArrayList<KeyValue>?): Request {
        val builder = Request.Builder()
        if (headers != null && !headers.isEmpty()) {
            for (keyValue in headers) {
                builder.addHeader(keyValue.key, keyValue.value)
            }
        }
        return builder.url(url).post(body).build()
    }

//    MediaType.parse("application/x-www-form-urlencoded; charset=utf-8")

    private fun getFormBody(contentType: MediaType, vararg params: String?): RequestBody {
        var sb: StringBuilder? = null
        if (params.isNotEmpty()) {
            val length = params.size
            if (length % 2 == 0) {
                sb = StringBuilder()
                val count = length / 2
                for (i in 0 until count) {
                    val value = params[i * 2 + 1]
                    if (!TextUtils.isEmpty(value)) {
                        sb.append(params[i * 2])
                        sb.append("=")
                        sb.append(value)
                        sb.append("&")
                    } else {
                        sb.append(params[i * 2])
                        sb.append("&")
                    }
                }
                val leng = sb.length
                if (leng > 0) {
                    sb.deleteCharAt(leng - 1)
                }
            }
        }
        if (autoKeyValues != null && !autoKeyValues!!.isEmpty()) {
            if (sb == null) {
                sb = StringBuilder()
            }
            for (keyValue in autoKeyValues!!) {
                sb.append(keyValue.key)
                sb.append("=")
                sb.append(keyValue.value)
                sb.append("&")
            }
            val leng = sb.length
            if (leng > 0) {
                sb.deleteCharAt(leng - 1)
            }
        }
        val param = if (sb == null) "" else sb.toString()
        return RequestBody.create(contentType, param)
    }

    /**
     * 获取一个请求的参数body
     *
     * @param params 已键值对格式传入, 如:key1, value1, key2, value2...
     * @return RequestBody 请求的表单体
     */
    private fun getFormBody(vararg params: String?): RequestBody {
        val builder: FormBody.Builder = FormBody.Builder()
        if (params.isNotEmpty()) {
            val length = params.size
            if (length % 2 == 0) {
                val count = length / 2
                for (i in 0 until count) {
                    val value = params[i * 2 + 1]
                    builder.add(params[i * 2]!!, value)
                    MyLog.e(params[i * 2] + "=" + value)
                }
            } else {
                MyLog.e("======== > params error")
            }
        }
        if (autoKeyValues != null && !autoKeyValues!!.isEmpty()) {
            for (keyValue in autoKeyValues!!) {
                builder.add(keyValue.key, keyValue.value)
            }
        }
        return builder.build()
    }

    // 创建一个文件上传请求
    private fun getMultipartBody(path: ArrayList<String>, vararg params: String?): RequestBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        if (params.isNotEmpty()) {
            val length = params.size
            if (length % 2 == 0) {
                val count = length / 2
                for (i in 0 until count) {
                    val value = params[i * 2 + 1]
                    if (!TextUtils.isEmpty(value)) {
                        builder.addFormDataPart(params[i * 2]!!, value!!)
                        MyLog.e(params[i * 2] + " = " + value)
                    }
                }
            } else {
                MyLog.e("======== > params error")
            }
        }
        if (!path.isEmpty()) {
            if (MEDIA_TYPE_PNG == null) { // 初始化图片类型
                MEDIA_TYPE_PNG = MediaType.parse("image/png")
            }
            var i = 0
            for (str in path) {
                val f = File(str)
                if (f.exists()) {
                    // 设置上传文件为  图片
                    builder.addFormDataPart("file_" + i++, f.name, RequestBody.create(MEDIA_TYPE_PNG, f))
                    MyLog.e("file_$i path = $str")
                } else {
                    MyLog.e("file_$i no exist,  path = $str")
                }
            }
        }
        return builder.build()
    }

    /**
     * 获取一个请求的参数body
     *
     * @param params      已键值对格式传入, 如:key1, value1, key2, value2...
     * @return RequestBody 请求的表单体
     */
    private fun getJsonFormBody(vararg params: String?): RequestBody {
        val map = HashMap<String, String>()
        if (params.isNotEmpty()) {
            val length = params.size
            if (length % 2 == 0) {
                val count = length / 2
                for (i in 0 until count) {
                    val value = params[i * 2 + 1]
                    if (!TextUtils.isEmpty(value)) {
                        map[params[i * 2]!!] = value!!
                    }
                }
            } else {
                MyLog.e("======== > params error")
            }
        }
        val p = com.model.cjx.common.JsonParser.toJson(map)
        return RequestBody.create(MediaType.parse("application/json"), p)
    }

    // 创建一个下载
    fun download(url: String, listener: ProgressResponseBody.ProgressResponseListener?, callback: Callback): Call {
        //构造请求
        val request = Request.Builder()
                .url(url)
                .build()
        val builder = OkHttpClient.Builder()
        if (listener != null) {
            builder.addNetworkInterceptor { chain ->
                val originalResponse = chain.proceed(chain.request())
                val body = ProgressResponseBody(originalResponse.body()!!, listener)
                originalResponse.newBuilder().body(body).build()
            }
        }
        val downloadClient = builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                //其他配置
                .build()
        val call = downloadClient.newCall(request)
        call.enqueue(callback)
        return call
    }

    // 创建一个上传
    fun upload(listener: ProgressRequestBody.ProgressRequestListener,
               callback: Callback, path: ArrayList<String>, url: String, vararg params: String?): Call? {
        return upload(null, listener, callback, path, url, *params)
    }

    // 创建一个带自定义header的上传
    fun upload(headers: ArrayList<KeyValue>?, listener: ProgressRequestBody.ProgressRequestListener?,
               callback: Callback, path: ArrayList<String>, url: String, vararg params: String?): Call? {
        //构造上传请求，类似web表单
        val requestBody = getMultipartBody(path, *params)
        val request = getRequest(url,
                if (listener == null) {
                    requestBody
                } else {
                    ProgressRequestBody(requestBody, listener)
                }, headers)
        return upload(callback, request)
    }

    private fun upload(callback: Callback, request: Request): Call? {
        var call: Call? = null
        try {
            call = client.newCall(request)
            call!!.enqueue(callback)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return call
    }


    // 返回一个忽略所有https证书的builder
    private fun getSSLHttpBuilder(): OkHttpClient.Builder {

        val trustAllCerts = object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) = Unit

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }
        val builder = OkHttpClient.Builder()
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, arrayOf(trustAllCerts), SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory, trustAllCerts)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        builder.hostnameVerifier { _, _ -> true }
        return builder
    }

    companion object {
        private var httpInstance: HttpUtils? = null
        private var httpsInstance: HttpUtils? = null

        /**
         * 获取一个HttpUtils单利对象
         */
        fun getInstance(): HttpUtils {
            if (httpInstance == null) {
                synchronized(HttpUtils::class.java) {
                    if (httpInstance == null) {
                        httpInstance = HttpUtils(false)
                    }
                }
            }
            return httpInstance!!
        }

        fun getHttpsInstance(stream: InputStream? = null): HttpUtils {
            if (httpsInstance == null) {
                synchronized(HttpUtils::class.java) {
                    if (httpsInstance == null) {
                        httpsInstance = HttpUtils(true, stream)
                    }
                }
            }
            return httpsInstance!!
        }

        const val MEDIATYPE_TEXT = 0
        const val MEDIATYPE_JSON = 1
    }
}
