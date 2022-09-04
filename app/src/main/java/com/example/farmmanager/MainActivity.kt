package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityMainBinding //databing(레이아웃 연동)을 위한 클래스
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.*
import java.lang.Thread.sleep
import java.net.Socket


class MainActivity : AppCompatActivity() {
    //전역변수
    companion object{
        var humity_in = 40
        var humity_out = 80
        var water_in = 40
        var water_out = 80
        var alarm = false
        val adapter = RecyclerAdapter()
    }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val helper = SqliteHelper(this,"logging",null,1)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) //setContentView에는 binding.root를 꼭 전달

        SocketReceiver().start()
        adapter.listData.addAll(helper.select()) //adapter의 listData에 DB에서 가져온 데이터를 세팅
        binding.recyclerMemo.adapter = adapter //메인 화면의 리사이클러뷰 위젯에 adpater을 연결
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)  //레이아웃 매니저를 설정

        val intent0 = Intent(this, BatActivity::class.java)
        val intent1 = Intent(this, CctvActivity::class.java)
        val intent2 = Intent(this, OptionActivity::class.java)


        //레이아웃의 버튼들과 연동 => 클릭 시 해당 엑티비티 화면을 띄우기

        binding.btbat.setOnClickListener { startActivity(intent0) }
        binding.btCCTV.setOnClickListener { startActivity(intent1) }
        binding.btSetting.setOnClickListener { startActivity(intent2) }

    }
}

class SocketReceiver : Thread() {
    //Receiver는 계속 유지되어야 한다
    override fun run() {

        Log.d("LUC_TAG","SocketReceiver 시작")
        //소켓 생성

        try{SocketClient.connect()}
        catch(e:java.lang.Exception){}
        Log.d("LUC_TAG","SocketReceiver 연결 완료")


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

class SocketClient : Serializable {
    companion object{
        val serverIP = "192.168.0.2" // 서버의 IP
        val port = 3022 // 서버의 port


        lateinit var originSocket: Socket
        lateinit var inputStream: InputStream
        lateinit var outputStream: OutputStream
        lateinit var datainputStream: DataInputStream
        lateinit var dataoutputStream: DataOutputStream

        var socketBtn = false

        fun connect() {
            try {
                originSocket = Socket(serverIP, port)
                outputStream = originSocket.getOutputStream()
                inputStream = originSocket.getInputStream()
                dataoutputStream = DataOutputStream(outputStream)
                datainputStream = DataInputStream(inputStream)
                socketBtn = true

            } catch (e: Exception) {
                Log.d("LUC_TAG","socket connect exception: $e")
                sleep(2000)
                Log.d("LUC_TAG","retry")
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