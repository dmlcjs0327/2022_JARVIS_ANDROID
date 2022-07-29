package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmmanager.databinding.ActivityOptionBinding

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