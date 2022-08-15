package com.example.a2022_jarvis_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class BatActivity : AppCompatActivity() {

    val binding by lazy { ActivityBatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack2.setOnClickListener {
            finish()
        }
    }
}