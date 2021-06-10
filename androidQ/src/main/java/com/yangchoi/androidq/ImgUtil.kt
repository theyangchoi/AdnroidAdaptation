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
import java.io.*


/**
 * Created on 2021/6/9
 * describe: 获取本地图片工具类
 */
object ImgUtil {
    /**
     * 图片信息集合
     * */
    private val IMAGE_PROJECTION = arrayOf(
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )
    /**
     * 获取本地图片
     * */
    fun getImage(context: Context):String?{
        val imageCursor: Cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[4] + " DESC")!!
        if (imageCursor.moveToNext()){
            var path = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]))
            val name = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]))
            val id = imageCursor.getInt(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]))
            val folderPath = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[3]))
            val folderName = imageCursor.getString(imageCursor.getColumnIndexOrThrow(IMAGE_PROJECTION[4]))
            //如果是Android Q 需要将文件保存到沙盒内在进行操作
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                /**
                 * 这个地址是沙盒地址
                 * 可以直接使用glide进行加载，但是不能做上传等操作
                 *
                 * content://media/external/images/media/1303839
                 * */
                path  = MediaStore.Images.Media
                    .EXTERNAL_CONTENT_URI
                    .buildUpon()
                    .appendPath(id.toString()).build().toString()
                /**
                 * 将文件保存到沙盒内,在对沙盒进行上传等操作
                 *
                 * /storage/emulated/0/Android/data/com.yangchoi.adnroidadaptation/cache/1917233809.jpg
                 * */
                val fileQ = saveUriToFile(context, Uri.parse(path))
                Log.e("imageTAG","地址:$path  沙盒地址:$fileQ")
                return fileQ.toString()
            }else{
                //在Android10 这个path无法直接显示
                //这个地址无法直接展示
                //需要在AndroidManifest.xml文件中添加以下代码android:requestLegacyExternalStorage="true"
                Log.e("imageTAG","path:$path  name:$name  id:$id  folderPath:$folderPath folderName:$folderName")
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


    /**
     * 通过MediaStore保存图片 保存后添加到相册数据库
     * @param context      context
     * @param sourceFile   源文件
     * @param saveFileName 保存的文件名
     * @param saveDirName  picture子目录
     * @return 成功或者失败
     */
    fun saveImageToPicture(context: Context, sourceFile: File, saveFileName: String?, saveDirName: String): Boolean {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DESCRIPTION, "description content")
        values.put(MediaStore.Images.Media.DISPLAY_NAME, saveFileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.TITLE, "Image.png")
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$saveDirName")
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val resolver = context.contentResolver
        val insertUri = resolver.insert(external, values)
        var inputStream: BufferedInputStream? = null
        var os: OutputStream? = null
        var result = false
        try {
            inputStream = BufferedInputStream(FileInputStream(sourceFile))
            if (insertUri != null) {
                os = resolver.openOutputStream(insertUri)
            }
            if (os != null) {
                val buffer = ByteArray(1024 * 4)
                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    os.write(buffer, 0, len)
                }
                os.flush()
            }
            result = true
        } catch (e: IOException) {
            result = false
        } finally {
            os?.close()
            inputStream?.close()
        }
        return result
    }
}