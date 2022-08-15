package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityBatBinding //databing(레이아웃 연동)을 위한 클래스

class BatActivity : AppCompatActivity() {

    val binding by lazy { ActivityBatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //값 초기화
        binding.textHL.text = MainActivity.humity_out.toString()
        binding.TextHL1.setText(MainActivity.humity_in.toString())


        //[확인] 버튼에 대한 바인딩
        binding.btHLset.setOnClickListener {
            //textHL의 text를 전역변수에 저장
            MainActivity.humity_in = Integer.parseInt(binding.TextHL1.text.toString())

            //정보 전송
            SocketSender().start()
        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack2.setOnClickListener {
            finish()
        }
    }
}