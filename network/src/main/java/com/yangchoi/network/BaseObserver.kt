package com.yangchoi.network

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
/**
 * @author y
 * @date 2020/10/13.
 * email：theyangchoi@163.com
 * description：数据返回统一处理
 */
abstract class BaseObserver<T> : Observer<BaseResponse<T>> {
    override fun onNext(response: BaseResponse<T>) {
        if (response.errorCode === 200) {
            onSuccess(response.data!!)
        } else {
            onFailure(null, response.errorMsg)
        }
    }

    override fun onError(e: Throwable) { //服务器错误信息处理
        onFailure(e,
            RxExceptionUtil.exceptionHandler(e)
        )
    }

    override fun onComplete() {}
    override fun onSubscribe(d: Disposable) {}
    abstract fun onSuccess(result: T)
    abstract fun onFailure(e: Throwable?, errorMsg: String?)

    companion object {
        private const val TAG = "BaseObserver"
    }
}