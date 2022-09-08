package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import android.util.Log
import com.example.farmmanager.databinding.ActivityOptionBinding //databing(레이아웃 연동)을 위한 클래스
import com.example.farmmanager.GlobalVariables as G

class OptionActivity : AppCompatActivity() {

    val binding by lazy { ActivityOptionBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //초기화
        binding.switchAlert.isChecked = (G.alarmOn==1)


        //[경고 메세지 수신 여부]에 대한 바인딩
        binding.switchAlert.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                G.alarmOn = 1
                Log.d("LOG_[OptionActivity]","[OptionActivity] 푸시 ON")
                G.toast("[OptionActivity] 푸시 ON")
            }
            else {
                G.alarmOn = 0
                Log.d("LOG_[OptionActivity]","[OptionActivity] 푸시 OFF")
                G.toast("[OptionActivity] 푸시 OFF")
            }
        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack4.setOnClickListener {
            finish()
        }
    }
}