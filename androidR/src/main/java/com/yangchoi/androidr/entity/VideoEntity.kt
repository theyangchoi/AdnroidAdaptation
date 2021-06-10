package com.yangchoi.androidr.entity

import android.net.Uri

/**
 * Created on 2021/6/10
 * describe:
 */
class VideoEntity(var uri: Uri? = null,
                  var name: String? = null,
                  var duration: Int = -1,
                  var size: Int = -1)