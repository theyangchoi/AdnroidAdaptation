package com.yangchoi.androidq

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.system.Os.close
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import java.io.*


/**
 * Created on 2021/6/9
 * describe: 文件夹工具类
 */
object FileUtil {
    val testPath = Environment.getExternalStorageDirectory().toString() + "/移动考勤/data/"

    /**
     * 判断文件夹是否存在
     *
     * 自Android Q开始，公有目录File API都失效，不能直接通过new File(path).exists();判断公有目录文件是否存在
     * */
    fun fileExists(context: Context):Boolean{
        if (context == null)
            return false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val testPahtQ = ContextCompat.getExternalFilesDirs(context, null)[0].getAbsolutePath()+"/data/"
            var afd: AssetFileDescriptor? = null
            val cr = context.contentResolver
            try {
                Log.e("pathTAG", testPahtQ)
                afd = cr.openAssetFileDescriptor(Uri.parse(testPath), "r")
                if (afd == null)
                    return false
                else
                    return true
            } catch (e: FileNotFoundException) {
                return false
            }
        }else{
            val watch: File = File(testPath)
            return watch.exists()
        }
    }

    /**
     * 创建文件夹
     * @param context
     * */
    fun createDirs(context: Context){
        /**
         * Android 10 不能在公共目录创建文件夹
         * 只能在/storage/emulated/0/Android/data/包名/files 此目录下创建
         *
         * ContextCompat.getExternalFilesDirs(context, null)[0].absolutePath获取到的就是/storage/emulated/0/Android/data/包名/files路径
         * testdirs就是创建的文件夹
         * */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            val path = ContextCompat.getExternalFilesDirs(context, null)[0].absolutePath + "/testdirs/"
            val dirFile = File(path)
            Log.e("mkdirsTAG","Q:"+dirFile.toString())
            if (!dirFile.exists()){
                val mkdirs = dirFile.mkdirs()
                if (!mkdirs)
                    Log.e("mkdirsTAG","Q : 创建:$mkdirs")
                else
                    Log.e("mkdirsTAG","Q : 创建成功:${dirFile.toString()}")
            }
        }else{
            //获取外部存储路径  api 29以下可以用这种方式在根目录 创建文件夹
            //直接创建在 storage/emulated/0/xxxfile
            val storage = Environment.getExternalStorageDirectory().path + "/testdirs"
            val dirFile = File(storage)
            if (!dirFile.exists()){
                val mkdirs = dirFile.mkdirs()
                if (!mkdirs)
                    Log.e("mkdirsTAG","文件夹创建失败")
                else
                    Log.e("mkdirsTAG","文件夹创建成功:${dirFile.toString()}")
            }
        }
    }

    /**
     * 创建文件
     * @param fileName 文件名
     * @param filePath 文件路径
     * */
    fun createFile(context: Context,filePath:String,fileName:String):File?{
        var file:File? = null
        var fileDirs = File(filePath)
        //判断文件夹是否存在
        if (fileDirs.exists()){
            try {
                file = File(filePath + fileName)
                if (!file.exists()){
                    file.createNewFile()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }else{
            //不存在就创建文件夹
            createDirs(context)
        }
        return file
    }

    /**
     * 往文件里面写入数据
     * @param content 要写入的内容
     * @param fileName 文件名称
     * @param filePath 文件夹路径
     * */
    fun writeContentToFile(content:String,filePath: String,fileName: String){
        val strFilePath = filePath+fileName

        val strContent = content + "\r\n"
        try {
            val file = File(strFilePath)
            if (file.exists()){
                Log.e("mkdirsTAG","文件存在写入数据")
                val raf = RandomAccessFile(file, "rwd")
                raf.seek(file.length())
                raf.write(strContent.toByteArray())
                raf.close()
            }else{
                Log.e("mkdirsTAG","文件不存在,请先创建文件")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }



    /**
     * 保存文件到公共目录  比如 picture  download等目录,这里用download目录作为例子
     * @param context
     * @param sourcePath 公共目录根地址storage/emulated/0/
     * @param fileName 保存的文件名称
     * @param saveDirName 保存的文件夹名称
     * */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveToStorageFile(context: Context, sourcePath:String, fileName:String, saveDirName:String){
        val values = ContentValues()
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName)
        /**
         * 文件类型
         * apk application/vnd.android.package-archive
         * 图片 image/png
         *
         * 要保存其他内容对此属性进行修改就行
         * */
        values.put(MediaStore.Downloads.MIME_TYPE, "application/vnd.android.package-archive")
        values.put(MediaStore.Downloads.RELATIVE_PATH, "Download/" + saveDirName + "/")

        val external = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val resolver: ContentResolver = context.getContentResolver()
        val insertUri = resolver.insert(external, values) ?: return
        val mFilePath = insertUri.toString()
        var `is`: InputStream? = null
        var os: OutputStream? = null
        try {
            os = resolver.openOutputStream(insertUri)
            if (os == null) {
                return
            }
            var read: Int
            val sourceFile: File = File(sourcePath)
            if (sourceFile.exists()) { // 文件存在时
                `is` = FileInputStream(sourceFile) // 读入原文件
                val buffer = ByteArray(1444)
                while (`is`.read(buffer).also { read = it } != -1) {
                    os.write(buffer, 0, read)
                }
                `is`.close()
                os.close()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            `is`?.close()
            os?.close()
        }
    }
}