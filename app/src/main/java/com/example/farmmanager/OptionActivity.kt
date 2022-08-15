package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityOptionBinding //databing(레이아웃 연동)을 위한 클래스

class OptionActivity : AppCompatActivity() {

    val binding by lazy { ActivityOptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //초기화
        binding.switchAlert.isChecked = MainActivity.alarm


        //[경고 메세지 수신 여부]에 대한 바인딩
        binding.switchAlert.setOnCheckedChangeListener { buttonView, isChecked ->
            MainActivity.alarm = isChecked
        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack4.setOnClickListener {
            finish()
        }
    }
}