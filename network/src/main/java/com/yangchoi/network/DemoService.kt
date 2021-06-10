package com.yangchoi.network

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created on 2021/6/9
 * describe:
 */
interface DemoService {

    @GET("banner/json")
    fun getBannerList():Call<BaseResponse<Any>>

}