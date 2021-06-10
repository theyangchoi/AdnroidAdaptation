package com.yangchoi.network

/**
 * @author y
 * @date 2020/10/13.
 * email：theyangchoi@163.com
 * description：统一响应请求头
 */
data class BaseResponse<T> (
    var errorCode:Int = 0,
    var errorMsg: String? = null,
    var data: T? = null
)