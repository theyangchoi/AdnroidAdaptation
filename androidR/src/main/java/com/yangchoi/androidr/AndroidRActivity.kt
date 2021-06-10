package com.yangchoi.androidr

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_android_r.*

/**
 * Created on 2021/6/10
 * describe:
 */
class AndroidRActivity : AppCompatActivity(),View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_r)

        btnGetPermission.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btnGetPermission ->{
                startActivity(Intent(this,AndroidRActivity::class.java))
            }
        }
    }

}