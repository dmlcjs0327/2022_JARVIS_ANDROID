//앱을 재실행해도 남아있어야 할 데이터
package com.example.farmmanager

import android.content.Context
import android.util.Log
import android.widget.Toast


object GlobalVariables {
    var alarmOn = false //푸시알람 설정 여부

    //받아올 정보(습도,T이상,S이상)
    var humityReal = 50 //실제 습도(컨트롤러가 보내준 습도)
    var tOdd = 0 //수위탱크 상태 / 0: 정상, 1: 이상
    var sOdd = 0 //스프링쿨러 상태 / 0: 정상, 1: 이상

    //전송할 정보(습도,경보,캡처)
    var humityTarget = 50 //목표 습도(앱에서 설정하는 습도)
    var sirenOption = 0 //사이렌 울리기 / 0: 안울림, 1: 울림)
    var captureOption = 0 //캡처주기 / 0: 끄기, 숫자: 하루 중 캡처할 빈도

    //소켓 관련
    val interval = 1000L //스레드를 정지할 시간 (밀리초)
    val serverIP = "192.168.0.2" // 서버의 IP
    val serverPort = 3022 // 서버의 port

    //임시 메세지창을 띄우는 함수
    fun toast(message: String) {
        Toast.makeText(MainApplication.applicationContext(), message, Toast.LENGTH_SHORT).show()
    }
}