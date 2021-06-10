package com.yangchoi.androidr

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * Created on 2021/6/10
 * describe: Android 11 获取外部存储权限
 */
class PermissionActivity : AppCompatActivity() {
    private val SUCCESS_CODE = 1111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有读写权限
            if (Environment.isExternalStorageManager()) {
                //todo
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + this.getPackageName())
                startActivityForResult(intent, SUCCESS_CODE)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
                //todo
            } else {
                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), SUCCESS_CODE)
            }
        } else {
            //todo
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SUCCESS_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //todo
            } else {
                //permission error
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SUCCESS_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //todo
            } else {
                //permission error
            }
        }
    }

    /**
     * Android 11 在申请定位权限的时候要对前后台定位权限进行分开申请,并且添加说明
     * */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun Context.checkBackgroundLocationPermissionAPI30(backgroundLocationRequestCode: Int) {
        val permissionAccessCoarseLocationApproved = ActivityCompat
            .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            val backgroundLocationPermissionApproved = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

            if (backgroundLocationPermissionApproved) {
                //前后台位置权限都有
            } else {
                //申请后台权限
                if (applicationInfo.targetSdkVersion < Build.VERSION_CODES.R){
                    ActivityCompat.requestPermissions(this@PermissionActivity,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        200)
                }else{
                    AlertDialog.Builder(this).setMessage("需要提供后台位置权限，请在设置页面选择始终允许")
                        .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this@PermissionActivity,
                                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                                200)
                        }).create().show()
                }

            }
        } else {
            if (applicationInfo.targetSdkVersion < Build.VERSION_CODES.R){
                //申请前台和后台位置权限
                ActivityCompat.requestPermissions(this@PermissionActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    100)
            }else{
                //申请前台位置权限
                ActivityCompat.requestPermissions(this@PermissionActivity,
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    100)
            }
        }

    }

    private fun Context.checkSinglePermission(permission: String) : Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 动态获取电话号码权限
     * 如果不获取权限会发生以下异常
     * java.lang.SecurityException: getLine1NumberForDisplay: Neither user 10151 nor current process has android.permission.READ_PHONE_STATE, android.permission.READ_SMS, or android.permission.READ_PHONE_NUMBERS
     * */
    private fun registerPhoneNumbersPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_PHONE_NUMBERS), 100)
    }
}