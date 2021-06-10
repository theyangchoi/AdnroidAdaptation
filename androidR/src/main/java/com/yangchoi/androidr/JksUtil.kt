package com.yangchoi.androidr

/**
 * Created on 2021/6/10
 * describe:
 */
object JksUtil {
    /**
     * 对于以 Android 11（API 级别 30）为目标平台，
     * 且目前仅使用 APK 签名方案 v1 签名的应用，
     * 现在还必须使用 APK 签名方案 v2 或更高版本进行签名。
     * 用户无法在搭载 Android 11 的设备上安装或更新仅通过 APK 签名方案 v1 签名的应用。
     *
     * 换句话说，如果你打包的时候没使用v2的签名，在 11的手机无法安装和更新
     * */
}