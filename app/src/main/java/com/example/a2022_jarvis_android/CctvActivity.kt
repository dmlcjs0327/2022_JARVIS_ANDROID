package com.example.a2022_jarvis_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class CctvActivity : AppCompatActivity() {

    val binding by lazy { ActivityCctvBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack3.setOnClickListener {
            finish()
        }
    }
}