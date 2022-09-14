//CCTV에 대한 엑티비티
package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import android.util.Log
import com.example.farmmanager.databinding.ActivityCctvBinding //databing(레이아웃 연동)을 위한 클래스
import android.webkit.WebViewClient
import com.example.farmmanager.GlobalVariables as G


class CctvActivity : AppCompatActivity() {

    val binding by lazy { ActivityCctvBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //초기화
        binding.switchAlert3.isChecked = (G.sirenOption==1)


        //[경고 메세지 수신 여부]에 대한 바인딩
        binding.switchAlert3.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                G.sirenOption = 1
                Log.d("LOG_[CctvActivity]","[CctvActivity] 사이렌 ON")
                //G.toast("[CctvActivity] 사이렌 ON")
                SocketSender().start()//정보 전송
                SocketSender().join()
            }
            else {
                G.sirenOption = 0
                Log.d("LOG_[CctvActivity]","[CctvActivity] 사이렌 OFF")
                //G.toast("[CctvActivity] 사이렌 OFF")
                SocketSender().start()//정보 전송
                SocketSender().join()
            }
        }

        binding.videoView.apply {
            webViewClient = WebViewClient()
            settings.useWideViewPort = true      // _09.04: wide 뷰포트 사용(넓게 보이기 위함)
            settings.javaScriptEnabled = true    // _09.04: 자바 스크립트 허용
            settings.loadWithOverviewMode = true // _09.04: 컨텐츠가 웹뷰보다 클 시 스크린에 맞게 조정
            settings.builtInZoomControls = true  // _09.04: 웹뷰 줌 기능 사용
            settings.supportZoom()               // _09.04: 웹뷰 줌 아이콘 사용
            settings.displayZoomControls = false // _09.04: 줌컨트롤러가 안보이게 설정
            binding.videoView.loadUrl(G.videoUrl)
        }

        //[나가기] 버튼에 대한 바인딩
        binding.btnBack3.setOnClickListener {
            finish()
        }
    }
}
