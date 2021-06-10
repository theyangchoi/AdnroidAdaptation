package com.yangchoi.network

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import io.reactivex.disposables.Disposable

/**
 * @author y
 * @date 2020/10/13.
 * email：theyangchoi@163.com
 * description：Observer加入加载框
 */
abstract class MyObserver<T> @JvmOverloads constructor(
    private val mContext: Context,
    private val mShowDialog: Boolean = true
) : BaseObserver<T>() {
    private var dialog: ProgressDialog? = null
    private var d: Disposable? = null
    override fun onSubscribe(d: Disposable) {
        this.d = d
        if (!isConnected(mContext)) {
            Toast.makeText(mContext, "未连接网络", Toast.LENGTH_SHORT).show()
            if (d.isDisposed) {
                d.dispose()
            }
        } else {
            if (dialog == null && mShowDialog == true) {
                dialog = ProgressDialog(mContext)
                dialog!!.setMessage("正在加载中")
                dialog!!.show()
            }
        }
    }

    override fun onError(e: Throwable) {
        if (d!!.isDisposed) {
            d!!.dispose()
        }
        hidDialog()
        super.onError(e)
    }

    override fun onComplete() {
        if (d!!.isDisposed) {
            d!!.dispose()
        }
        hidDialog()
        super.onComplete()
    }

    fun hidDialog() {
        if (dialog != null && mShowDialog == true) dialog!!.dismiss()
        dialog = null
    }

    /**
     * 取消订阅
     */
    fun cancleRequest() {
        if (d != null && d!!.isDisposed) {
            d!!.dispose()
            hidDialog()
        }
    }

    companion object {
        /**
         * 是否有网络连接，不管是wifi还是数据流量
         * @param context
         * @return
         */
        fun isConnected(context: Context): Boolean {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo ?: return false
            return info.isAvailable
        }
    }

}