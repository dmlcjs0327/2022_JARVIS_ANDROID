package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityCctvBinding //databing(레이아웃 연동)을 위한 클래스

class CctvActivity : AppCompatActivity() {

    val binding by lazy { ActivityCctvBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack3.setOnClickListener {
            finish()
        }
    }
}