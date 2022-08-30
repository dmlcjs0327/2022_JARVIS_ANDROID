package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityMainBinding //databing(레이아웃 연동)을 위한 클래스
import android.content.Intent
import android.util.Log
import java.io.*
import java.lang.Thread.sleep
import java.net.Socket

//메인 클래스
class MainActivity : AppCompatActivity() {

    //전역변수
    companion object{
        var humity_in = 40
        var humity_out = 80
        var water_in = 40
        var water_out = 80
        var alarm = false
    }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("[LOG]","MainActivity - onCreate: 시작")
        setContentView(binding.root) //setContentView에는 binding.root를 꼭 전달
        SocketReceiver().start() //SocketReceiver 시작

        val intent0 = Intent(this, BatActivity::class.java) //Bat intent
        val intent1 = Intent(this, CctvActivity::class.java) //Cctv intent
        val intent2 = Intent(this, OptionActivity::class.java) //Option intent

        //레이아웃의 버튼들과 연동 => 클릭 시 해당 엑티비티 화면을 띄우기
        binding.btbat.setOnClickListener { startActivity(intent0) }
        binding.btCCTV.setOnClickListener { startActivity(intent1) }
        binding.btSetting.setOnClickListener { startActivity(intent2) }
    }
}

//컨트롤러에게서 데이터를 받아올 클래스
class SocketReceiver : Thread() {
    
    override fun run() { //thread를 시작 시, 이 부분이 실행

        Log.d("[LOG]","SocketReceiver: 시작")

        //1) 소켓 생성
        try{SocketClient.connect()}
        catch(e:java.lang.Exception){}
        Log.d("[LOG]","SocketReceiver 연결 완료")


        //data 받기 (밭습도,물높이)
        while(true){
            try{
                var recvData: String? = null
                while (recvData == null) recvData = SocketClient.read()
                Log.d("LUC_TAG","\n[수신된 데이터]: $recvData")

                var tmp = recvData.split(",")
                MainActivity.humity_out = Integer.parseInt(tmp[0])
                MainActivity.water_out = Integer.parseInt(tmp[1])
                sleep(1000)

            }catch (e:java.lang.Exception){
                SocketClient.socketBtn = false
                Log.d("LUC_TAG","SocketReceiver에 에러 발생. 재시도 중..")
                sleep(1000)
                while(true){
                    try{
                        if(SocketClient.socketBtn == false) SocketClient.connect()
                        break
                    }catch(e: java.lang.Exception){}
                }
                Log.d("LUC_TAG","SocketReceiver 재시작")
            }

        }

        SocketClient.closeConnect()
        Log.d("LUC_TAG","SocketReveiver 종료")
    }
}


class SocketSender : Thread() {
    //Sender는 필요할 때만 생성되면 된다
    override fun run() {

        Log.d("LUC_TAG","SocketSender 시작")
        //소켓 생성
        while(true){
            try{
                if(SocketClient.socketBtn == false) SocketClient.connect()
                break
            }catch(e: java.lang.Exception){}
        }

        Log.d("LUC_TAG","SocketSender 연결 완료")

        while(true){
            try{
                //data 보내기
                var data = "${MainActivity.humity_in},${MainActivity.water_in}"
                SocketClient.sendData(data)
                Log.d("LUC_TAG","\n[송신할 데이터]: $data")

                SocketClient.closeConnect()
                Log.d("LUC_TAG","SocketSender 종료")
                break
            }catch (e:java.lang.Exception){
                SocketClient.socketBtn = false
                Log.d("LUC_TAG","SocketSender에 에러 발생. 재시도 중..")
                sleep(1000)
                while(true){
                    try{
                        if(SocketClient.socketBtn == false) SocketClient.connect()
                        break
                    }catch(e: java.lang.Exception){}
                }

                Log.d("LUC_TAG","SocketSender 재시작")
            }

        }

    }
}


//소켓에 대한 클래스
class SocketClient : Serializable { //데이터 변환을 위해, Serializable 상속

    companion object{ //생성한 소켓을 통해 데이터 송수신을 모두 해야하므로, 전역으로 설정
        val serverIP = "192.168.0.2" // 서버의 IP (현재 이의천-노트북 IP로 설정되어 있음)
        val port = 3022 // 접근할 port (변경 가능)

        //사용할 stream들을 선언 (지연 초기화)
        lateinit var originSocket: Socket //소켓 객체
        lateinit var inputStream: InputStream //데이터를 받아올 스트림
        lateinit var outputStream: OutputStream //데이터를 전송할 스트림
        lateinit var datainputStream: DataInputStream //input에 대한 래퍼스트림
        lateinit var dataoutputStream: DataOutputStream //output에 대한 래퍼스트림

        var isConnect = false //소켓이 생성되었는지에 대한 변수

        //소켓을 생성 후, 서버와 연결
        fun connect(): Boolean {
            try {

                originSocket = Socket(serverIP, port) //소켓객체 생성

                outputStream = originSocket.getOutputStream()
                inputStream = originSocket.getInputStream()

                dataoutputStream = DataOutputStream(outputStream)
                datainputStream = DataInputStream(inputStream)

                isConnect = true

            } catch (e: Exception) {
                Log.d("[LOG]","socket connect error: $e")
                sleep(2000)
                Log.d("[LOG]","retry")
                connect()
            }
        }

        fun sendData(data: String) {
//        outputStream.write(data.toByteArray(Charsets.UTF_8))
            dataoutputStream.writeUTF(data)
        }

        fun read(): String? {
            if (inputStream.available() > 0) {
//            return inputStream.bufferedReader(Charsets.UTF_8).toString()
                val msg = ByteArray(inputStream.available())
                inputStream.read(msg)
                return msg.toString(Charsets.UTF_8)
            }
            return null
        }

        fun closeConnect() {
            outputStream.close()
            inputStream.close()
            originSocket.close()
        }
    }
}