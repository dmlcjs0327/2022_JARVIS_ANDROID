package com.example.farmmanager



import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import android.widget.SeekBar
import com.example.farmmanager.GlobalVariables as G
import com.example.farmmanager.databinding.ActivityBatBinding //databing(레이아웃 연동)을 위한 클래스



class BatActivity : AppCompatActivity() {

    val binding by lazy { ActivityBatBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val helper = MainActivity.logHelper
        val adapter = MainActivity.logAdapter

        //값 초기화
        binding.TextHL1.setText(G.humityTarget.toString()) //목표 습도
        binding.textHL.text = G.humityReal.toString() //실제 습도

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
            //textHL의 text를 전역변수에 저장
            MainActivity.humity_in = Integer.parseInt(binding.TextHL1.text.toString())

            //정보 전송
            SocketSender().start()

            if(binding.TextHL1.text.toString().isNotEmpty()){
                val logging = Logging(null,binding.TextHL1.text.toString(),System.currentTimeMillis())
                helper.insert(logging)
            } //TextHL1 위젯에 값이있으면 해당 내용으로 Logging 데이터 클래스를 생성하고 helper 클래스의 insert 메서드에 전달하여 DB에 저장

            adapter.listData.clear() //어뎁터의 데이터를 모두 초기화

            adapter.listData.addAll(helper.select())
            adapter.notifyDataSetChanged()  //DB에서 새로운 목록을 읽어와서 어댑터에 세팅하고 갱신함
        }


        //[나가기] 버튼에 대한 바인딩
        binding.btnBack2.setOnClickListener {
            finish()
        }
    }
}