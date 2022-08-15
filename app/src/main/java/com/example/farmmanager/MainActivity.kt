package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.farmmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent0 = Intent(this, NonActivity::class.java)
        val intent1 = Intent(this, BatActivity::class.java)
        val intent2 = Intent(this, CctvActivity::class.java)
        val intent3 = Intent(this, OptionActivity::class.java)

        binding.btnon.setOnClickListener { startActivity(intent0) }
        binding.btbat.setOnClickListener { startActivity(intent1) }
        binding.btCCTV.setOnClickListener { startActivity(intent2) }
        binding.btSetting.setOnClickListener { startActivity(intent3) }
    }
}