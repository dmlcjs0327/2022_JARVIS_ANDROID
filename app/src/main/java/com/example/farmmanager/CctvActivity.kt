package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmmanager.databinding.ActivityCctvBinding

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