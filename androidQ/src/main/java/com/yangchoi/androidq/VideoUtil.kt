package com.yangchoi.androidq

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.FileUtils
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream


/**
 * Created on 2021/6/9
 * describe: 获取本地视频工具类
 */
object VideoUtil {

    /**
     * 视频信息集合
     * */
    private val VIDEO_PROJECTION = arrayOf(
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media._ID
    )

    fun getVideo(context: Context):String?{
        val videoCursor:Cursor =
            context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEO_PROJECTION,null,null, null)!!

        if (videoCursor.moveToNext()){
            var path = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VIDEO_PROJECTION[0]))
            val name = videoCursor.getString(videoCursor.getColumnIndexOrThrow(VIDEO_PROJECTION[1]))
            val id = videoCursor.getInt(videoCursor.getColumnIndexOrThrow(VIDEO_PROJECTION[2]))

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                /**
                 * 这个地址是沙盒地址
                 * 可以直接使用glide进行加载，但是不能做上传等操作
                 *
                 * content://media/external/video/media/1302869
                 * */
                path  = MediaStore.Video.Media
                    .EXTERNAL_CONTENT_URI
                    .buildUpon()
                    .appendPath(id.toString()).build().toString()
                /**
                 * 将文件保存到沙盒内,在对沙盒进行上传等操作
                 *
                 * /storage/emulated/0/Android/data/com.yangchoi.adnroidadaptation/cache/c8f0cbd3bb674544b6756c1f4d2333ee.mp4
                 * */
                val fileQ = saveUriToFile(context, Uri.parse(path))
                Log.e("videoTAG","地址:$path  沙盒地址:$fileQ")
                return fileQ.toString()
            }else{
                //在Android10 这个path无法直接显示
                //这个地址无法直接展示
                //需要在AndroidManifest.xml文件中添加以下代码android:requestLegacyExternalStorage="true"
                Log.e("videoTAG","path:$path  name:$name  id:$id")
                return path
            }

        }else
            return null
    }

    /**
     * AndroidQ 要将文件保存到沙盒进行操作
     * 这里的几个判断要注意
     * if  uri.scheme == ContentResolver.SCHEME_FILE 如果文件路径是file://开头则直接转成file文件
     * else if uri.scheme == ContentResolver.SCHEME_CONTENT 如果文件路径是content://media开头则把文件放到沙盒里面去再操作
     * */
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveUriToFile(context: Context, uri: Uri): File?{
        if (uri.scheme == ContentResolver.SCHEME_FILE)
            return File(requireNotNull(uri.path))
        else if (uri.scheme == ContentResolver.SCHEME_CONTENT){
            //
            val contentResolver = context.contentResolver
            val displayName = run {
                val cursor = contentResolver.query(uri, null, null, null, null)
                cursor?.let {
                    if(it.moveToFirst())
                        it.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    else null
                }
            }
            val ios = contentResolver.openInputStream(uri)
            if (ios != null){
                return File("${context.externalCacheDir!!.absolutePath}/$displayName")
                    .apply {
                        val fos = FileOutputStream(this)
                        FileUtils.copy(ios, fos)
                        fos.close()
                        ios.close()
                    }
            }else
                return null
        }else
            return null
    }
}