package com.example.a2022_jarvis_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class OptionActivity : AppCompatActivity() {

    val binding by lazy { ActivityOptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack4.setOnClickListener {
            finish()
        }
    }
}