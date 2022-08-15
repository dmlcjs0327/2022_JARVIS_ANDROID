package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmmanager.databinding.ActivityBatBinding

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