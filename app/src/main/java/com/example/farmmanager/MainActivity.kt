package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityMainBinding //databing(레이아웃 연동)을 위한 클래스
import android.content.Intent


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) //setContentView에는 binding.root를 꼭 전달

        val intent0 = Intent(this, NonActivity::class.java)
        val intent1 = Intent(this, BatActivity::class.java)
        val intent2 = Intent(this, CctvActivity::class.java)
        val intent3 = Intent(this, OptionActivity::class.java)


        //레이아웃의 버튼들과 연동(클릭 시의 동작 설정)
        binding.btnon.setOnClickListener { startActivity(intent0) }
        binding.btbat.setOnClickListener { startActivity(intent1) }
        binding.btCCTV.setOnClickListener { startActivity(intent2) }
        binding.btSetting.setOnClickListener { startActivity(intent3) }
    }
}