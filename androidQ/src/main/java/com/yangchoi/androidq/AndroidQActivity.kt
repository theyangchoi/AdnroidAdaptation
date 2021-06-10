package com.yangchoi.androidq

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.yangchoi.androidq.*
import com.yangchoi.network.BaseResponse
import com.yangchoi.network.DemoService
import com.yangchoi.network.RequestUtils
import kotlinx.android.synthetic.main.activity_android_q.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created on 2021/6/9
 * describe:
 */
class AndroidQActivity : AppCompatActivity(),View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_q)

        btnGetData.setOnClickListener(this)
        btnGetImg.setOnClickListener(this)
        btnGetVideo.setOnClickListener(this)
        btnFileExists.setOnClickListener(this)
        btnCreateFile.setOnClickListener(this)
        btnWriteContent.setOnClickListener(this)
        btnSaveStorageFile.setOnClickListener(this)
        btnInStallAPK.setOnClickListener(this)
        btnGetPermission.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnGetData ->{
                getBannerData()
            }

            R.id.btnGetImg ->{
                Glide.with(this)
                    .load(ImgUtil.getImage(this))
                    .into(img)
            }

            R.id.btnGetVideo ->{
                videoview.setVideoURI(Uri.parse(VideoUtil.getVideo(this)))
                videoview.start()
            }

            R.id.btnFileExists ->{
                FileUtil.createDirs(this)
            }

            R.id.btnCreateFile ->{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    val filePath = ContextCompat.getExternalFilesDirs(this, null)[0].absolutePath + "/testdirs/"
                    FileUtil.createFile(this,filePath,"test.txt")
                }else{
                    val filePath = Environment.getExternalStorageDirectory().path + "/testdirs"
                    FileUtil.createFile(this,filePath,"test.txt")
                }
            }

            R.id.btnWriteContent ->{
                val filePath = ContextCompat.getExternalFilesDirs(this, null)[0].absolutePath + "/testdirs/"
                val fileName = "test.txt"
                FileUtil.writeContentToFile("这是新内容",filePath, fileName)
            }

            R.id.btnSaveStorageFile ->{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    val sourcePath = "storage/emulated/0/"
                    FileUtil.saveToStorageFile(this,sourcePath,"test.png","testStorage")
                }
            }

            R.id.btnInStallAPK ->{
                IntentUtil.installApk(this,"content://storage/emulated/0/Download/testStorage/test.apk","")
            }

            R.id.btnGetPermission ->{
                startActivity(Intent(this,PermissionActivity::class.java))
            }
        }
    }

    private fun getBannerData(){
        RequestUtils.defaultRetrofit.create(DemoService::class.java)
            .getBannerList()
            .enqueue(object : Callback<BaseResponse<Any>> {
                override fun onResponse(call: Call<BaseResponse<Any>>, response: Response<BaseResponse<Any>>) {
                    Log.e("networkTAG","onResponse:${Gson().toJson(response.body())}")
                }

                override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                    Log.e("networkTAG","onFailure:${t.message}")
                }

            })
    }

}