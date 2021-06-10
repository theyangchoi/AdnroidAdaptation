package com.yangchoi.androidr

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

/**
 * Created on 2021/6/10
 * describe: 获取5G网络工具类
 */
object NetworkUtil {

    /**
     * 获取网络类型,判断是否是5G
     * */
    private fun getNetworkType(context: Context){
        val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        tManager.listen(object : PhoneStateListener() {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun onDisplayInfoChanged(telephonyDisplayInfo: TelephonyDisplayInfo) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    return
                }
                super.onDisplayInfoChanged(telephonyDisplayInfo)

                when(telephonyDisplayInfo.networkType) {
                    TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO -> Log.e("networkTAG","高级专业版 LTE (5Ge)")
                    TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA -> Log.e("networkTAG","NR (5G) - 5G Sub-6 网络")
                    TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE -> Log.e("networkTAG","5G+/5G UW - 5G mmWave 网络")
                    else -> Log.e("networkTAG","other")
                }
            }

        }, PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED)
    }
    /**
     * 判断是否按流量计费
     *
     * 如果 isNotFlowPay 是true则不是按流量计费
     * */
    fun temp(context: Context){
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)

                    //true 代表连接不按流量计费
                    val isNotFlowPay=networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) ||
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED)
                }
            })
        }
    }
}