package com.yangchoi.adnroidadaptation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yangchoi.androidq.AndroidQActivity
import com.yangchoi.androidr.AndroidRActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAndroid10.setOnClickListener(this)
        btnAndroid11.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAndroid10 -> startActivity(Intent(this@MainActivity, AndroidQActivity::class.java))
            R.id.btnAndroid11 -> startActivity(Intent(this@MainActivity, AndroidRActivity::class.java))
        }
    }
}