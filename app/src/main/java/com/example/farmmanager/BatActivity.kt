package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import android.util.Log
import android.widget.SeekBar
import com.example.farmmanager.databinding.ActivityBatBinding //databing(레이아웃 연동)을 위한 클래스

class BatActivity : AppCompatActivity() {

    val binding by lazy { ActivityBatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //값 초기화
        binding.TextHL1.setText(MainActivity.humity_in.toString())
        binding.textHL.text = MainActivity.humity_out.toString()

        //[seekbar2]에 대한 바인딩
        binding.seekBar2.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //SeekBar2를 움직이면, 해당 TextHL1의 문자를 바꾼다
                binding.TextHL1.setText("$progress")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        //[스프링쿨러 작동] 버튼에 대한 바인딩
        binding.btHLset.setOnClickListener {

        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack2.setOnClickListener {
            finish()
        }
    }
}