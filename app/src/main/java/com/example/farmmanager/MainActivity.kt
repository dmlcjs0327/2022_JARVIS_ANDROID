//메인엑티비티
package com.example.farmmanager



import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityMainBinding //databing(레이아웃 연동)을 위한 클래스
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager



//메인엑티비티 클래스
class MainActivity : AppCompatActivity() {

    companion object{
        //로그에 대한 sql helper & 리사이클러뷰에 대한 adapter
        val logHelper = LogDBHelper(MainApplication.applicationContext(),"logging",null,1)
        val logAdapter = LogDBRecyclerAdapter()
    }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    //엑티비티 실행 시 동작코드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) //setContentView 에는 binding.root 를 꼭 전달
        SocketReceiver().start() //SocketReceiver 시작

        //로그에 대한 sql helper & 리사이클러뷰에 대한 adapter 설정
        logAdapter.listData.addAll(logHelper.select()) //adapter 의 listData 에 DB 에서 가져온 데이터를 세팅
        logAdapter.helper = logHelper

        binding.recyclerMemo.adapter = logAdapter //메인 화면의 리사이클러뷰 위젯에 adapter 를 연결
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)  //레이아웃 매니저를 설정

        val intent0 = Intent(this, BatActivity::class.java)
        val intent1 = Intent(this, CctvActivity::class.java)
        val intent2 = Intent(this, OptionActivity::class.java)
        val intent3 = Intent(this, DiaryActivity::class.java)

        //레이아웃의 버튼들과 연동 => 클릭 시 해당 엑티비티 화면을 띄우기
        binding.btbat.setOnClickListener { startActivity(intent0) }
        binding.btCCTV.setOnClickListener { startActivity(intent1) }
        binding.btSetting.setOnClickListener { startActivity(intent2) }
        binding.btdiary.setOnClickListener { startActivity(intent3) }
    }
}

