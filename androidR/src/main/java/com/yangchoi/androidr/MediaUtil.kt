package com.yangchoi.androidr

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.yangchoi.androidr.entity.VideoEntity
import java.util.concurrent.TimeUnit

/**
 * Created on 2021/6/10
 * describe: 媒体操作工具类  图片  视频  音频等
 *           11 主要是媒体集的操作
 */
object MediaUtil {

    val videoList = mutableListOf<VideoEntity>()

    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.SIZE
    )
    val selection = "${MediaStore.Video.Media.DURATION} >= ?"
    val selectionArgs = arrayOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString())
    val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

    fun getVideoList(context: Context){
        val listCursor:Cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder)!!

        val idColumn = listCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn = listCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        val durationColumn = listCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
        val sizeColumn = listCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

        while (listCursor.moveToNext()){
            val id = listCursor.getLong(idColumn)
            val name = listCursor.getString(nameColumn)
            val duration = listCursor.getInt(durationColumn)
            val size = listCursor.getInt(sizeColumn)

            val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            videoList += VideoEntity(contentUri, name, duration, size)
        }
    }
}