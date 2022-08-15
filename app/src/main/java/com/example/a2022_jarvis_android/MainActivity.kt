package com.example.a2022_jarvis_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a2022_jarvis_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) 		//setContentView에는 binding.root를 꼭 전달
        binding.myButton.setOnClickListener{	//레이아웃의 myButton과 연동(클릭 시의 동작 설정)
            binding.textView.text = "바인딩"	//myButton을 클릭 시 textView의 글자를 "바인딩"으로 변경
        }
    }
}