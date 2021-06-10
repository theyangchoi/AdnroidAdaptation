package com.yangchoi.network

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author y
 * @date 2020/10/13.
 * email：theyangchoi@163.com
 * description：
 */
object RequestUtils {
    private val client:OkHttpClient

    init {
        client = OkHttpClient().newBuilder()
            .readTimeout(Constants.DEFAULT_TIME.toLong(), TimeUnit.SECONDS) //设置读取超时时间
            .connectTimeout(Constants.DEFAULT_TIME.toLong(), TimeUnit.SECONDS) //设置请求超时时间
            .writeTimeout(Constants.DEFAULT_TIME.toLong(), TimeUnit.SECONDS) //设置写入超时时间
            .addInterceptor(LogInterceptor()) //添加打印拦截器
            .retryOnConnectionFailure(true) //设置出现错误进行重新连接。
            .build()
    }
    val defaultRetrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Constants.BASE_URL) //添加GSON解析：返回数据转换成GSON类型
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
val Context.defaultRetrofit: Retrofit
    get() = RequestUtils.defaultRetrofit