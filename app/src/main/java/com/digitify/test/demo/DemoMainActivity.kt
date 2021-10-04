package com.digitify.test.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.digitify.test.R
import dagger.hilt.android.AndroidEntryPoint
import com.yap.base.utills.BIO_METRIC_SCREEN_TYPE

@AndroidEntryPoint
class DemoMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_main)
    }
}