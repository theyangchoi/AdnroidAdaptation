package com.yangchoi.androidq

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File


/**
 * Created on 2021/6/10
 * describe: intent 工具类
 */
object IntentUtil {

    /**
     * 安装apk
     * */
    fun installApk(context: Context,filePath:String,saveFileName:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //适配Android Q,注意filePath是通过ContentResolver得到的，上述有相关代码
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(filePath), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(intent)
            return
        }

        val file = File(saveFileName.toString() + "osc.apk")
        if (!file.exists()) return
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri: Uri = FileProvider.getUriForFile(context, "net.oschina.app.provider", file)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}