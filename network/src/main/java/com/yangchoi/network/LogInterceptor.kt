package com.yangchoi.network

import okhttp3.*
import java.io.IOException

/**
 * @author y
 * @date 2020/10/13.
 * email：theyangchoi@163.com
 * description：Log拦截器
 */
class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()

        val t1 = System.nanoTime()
        val response = chain.proceed(chain.request())
        val t2 = System.nanoTime()

        val mediaType = response.body()!!.contentType()
        val content = response.body()!!.string()
//        LogUtils.e("LogInterceptor",
//            "url:${url}",
//            "header:${response.headers()}",
//            "response body:$content"
//            )

        return response.newBuilder()
//            .addHeader("token", GpsService.getToken())
            .body(ResponseBody.create(mediaType, content))
            .build()
    }
}