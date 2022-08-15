package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.example.farmmanager.databinding.ActivityNonBinding //databing(레이아웃 연동)을 위한 클래스
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

class NonActivity : AppCompatActivity() {

    val binding by lazy { ActivityNonBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //값 초기화
        binding.seekBar.progress = MainActivity.water_in
        binding.WL.height = MainActivity.water_in
        binding.TextWL1.setText("${binding.seekBar.progress}")
        binding.TextWL.text = MainActivity.water_in.toString()


        //[seekBar]에 대한 바인딩
        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //seekBar를 움직이면, 해당 값으로 TextWL1의 문자를 바꾼다
                binding.TextWL1.setText("$progress")

                //seekBar를 움직이면, 해당 값만큼 WL(물)의 크기를 변경한다
                binding.WL.height = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })


        //[물높이 입력창]에 대한 바인딩
        binding.TextWL1.addTextChangedListener(object : TextWatcher{
            //입력값이 1자리 이상의 숫자인 경우에만 seekBar와 TextWL1의 text를 갱신한다
            override fun afterTextChanged(s: Editable?) {
                if(s.toString().length>=1){
                    binding.seekBar.progress = Integer.parseInt(s.toString())
                    binding.TextWL1.setSelection(s.toString().length)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        //[확인] 버튼에 대한 바인딩
        binding.btWLset.setOnClickListener {
            //TextWL의 text를 전역변수에 저장
            MainActivity.water_in = Integer.parseInt(binding.TextWL1.text.toString())

            //정보 전송
            SocketSender().start()
        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack1.setOnClickListener {
            finish()
        }
    }
}