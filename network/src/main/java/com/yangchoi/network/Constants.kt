package com.yangchoi.network

/**
 * @author y
 * @date 2020/10/13.
 * email：theyangchoi@163.com
 * description：网络部分,常量配置
 */
object Constants {
    @JvmStatic
    val BASE_URL:String by lazy { if (true) "http://www.wanandroid.com/" else "https://www.wanandroid.com/"}

    const val DEFAULT_TIME = 10
    const val DEFAULT_TIME_UPLOAD = 300*1000
    const val retrofit = "value/5"
    const val retrofitList = "value"
}