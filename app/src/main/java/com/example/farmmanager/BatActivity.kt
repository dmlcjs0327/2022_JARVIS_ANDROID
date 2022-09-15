//밭습도에 대한 엑티비티
package com.example.farmmanager



import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import com.example.farmmanager.GlobalVariables as G
import com.example.farmmanager.databinding.ActivityBatBinding //databing(레이아웃 연동)을 위한 클래스


class BatActivity : AppCompatActivity() {

    val binding by lazy { ActivityBatBinding.inflate(layoutInflater) }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val helper = MainActivity.logHelper
        val adapter = MainActivity.logAdapter

        //값 초기화
        binding.TextHL1.setText(G.humityTarget.toString()) //목표습도 텍스트를 최신화
        binding.seekBar2.progress = G.humityTarget //시크바를 최신화
        binding.textHL.text = G.humityReal.toString() //실제습도 텍스트를 최신화
        binding.switchAlert2.isChecked = (G.motorOption==1) //모터옵션 최신화


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


        //[설정된 밭 습도]에 대한 바인딩
        binding.TextHL1.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(s.toString().isNotEmpty()){ //입력된 글자가 1자리 이상인 경우
                    binding.seekBar2.progress = Integer.parseInt(s.toString()) //시크바 최신화
                    binding.TextHL1.setSelection(s.toString().length) //커서를 끝으로
                }
            }
        })


        //[설정 습도 전송] 버튼에 대한 바인딩
        binding.btHLset.setOnClickListener {
            G.humityTarget = Integer.parseInt(binding.TextHL1.text.toString())//textHL의 text를 전역변수에 저장
            SocketSender().start()//정보 전송
            SocketSender().join()
            Log.d("LOG_[BatActivity]","[BatActivity] 정보 전송")
            //G.toast("[BatActivity] 정보 전송 완료")

            if(G.isSend &&binding.TextHL1.text.toString().isNotEmpty()){
                var id = (adapter.listData.size).toLong()
                val logging = Logging(id,binding.TextHL1.text.toString(),System.currentTimeMillis())
                helper.insert(logging)
                adapter.listData.clear() //어뎁터의 데이터를 모두 초기화
                adapter.listData.addAll(helper.select())
                adapter.notifyDataSetChanged()//DB 에서 새로운 목록을 읽어와서 어댑터에 세팅하고 갱신함
            } //Logging 데이터 클래스를 생성하고 helper 클래스의 insert 메서드에 전달하여 DB에 저장
        }


        //[식물 사진 캡처] 버튼에 대한 바인딩
        binding.btHLset2.setOnClickListener {
            SocketFileSender().start() //신호 전달
            SocketFileSender().join()
            Log.d("LOG_[BatActivity]","[BatActivity] 사진 캡처 완료")
            G.toast("[BatActivity] 사진 캡처 완료")

        }

        //[모터 강제 작동] 스위치에 대한 바인딩
        binding.switchAlert2.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                G.motorOption = 1
                SocketSender().start()//정보 전송
                SocketSender().join()
                Log.d("LOG_[BatActivity]","[BatActivity] 모터 강제 작동 시작")
                //G.toast("[BatActivity] 모터 강제 작동 시작")
            }
            else {
                G.motorOption = 0
                SocketSender().start()//정보 전송
                SocketSender().join()
                Log.d("LOG_[BatActivity]","[BatActivity] 모터 강제 작동 중지")
                //G.toast("[BatActivity] 모터 강제 작동 중지")
            }
        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack2.setOnClickListener {
            finish()
        }
    }
}