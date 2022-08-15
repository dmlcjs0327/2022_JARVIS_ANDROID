package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityMainBinding //databing(레이아웃 연동)을 위한 클래스
import android.content.Intent
import android.util.Log
import java.io.*
import java.net.Socket


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
        setContentView(binding.root) //setContentView에는 binding.root를 꼭 전달
        SocketReceiver().start()

        val intent0 = Intent(this, NonActivity::class.java)
        val intent1 = Intent(this, BatActivity::class.java)
        val intent2 = Intent(this, CctvActivity::class.java)
        val intent3 = Intent(this, OptionActivity::class.java)


        //레이아웃의 버튼들과 연동 => 클릭 시 해당 엑티비티 화면을 띄우기
        binding.btnon.setOnClickListener { startActivity(intent0) }
        binding.btbat.setOnClickListener { startActivity(intent1) }
        binding.btCCTV.setOnClickListener { startActivity(intent2) }
        binding.btSetting.setOnClickListener { startActivity(intent3) }
    }
}

class SocketReceiver : Thread() {
    //Receiver는 계속 유지되어야 한다
    override fun run() {

        //소켓 생성
        val socket = SocketClient()
        socket.connect(socket.serverIP,socket.port)


        //data 받기 (밭습도,물높이)
        while(true){
            try{
                var recvData: String? = null
                while (recvData == null) recvData = socket.read()
                Log.d("SOCKET","\n[수신된 데이터]: $recvData")

                var tmp = recvData.split(",")
                MainActivity.humity_out = Integer.parseInt(tmp[0])
                MainActivity.water_out = Integer.parseInt(tmp[1])

            }catch (e:java.lang.Exception){
                break;
            }

        }

        socket.closeConnect()
        Log.d("SOCKET","SocketReveiver 종료")
    }
}


class SocketSender : Thread() {
    //Sender는 필요할 때만 생성되면 된다
    override fun run() {

        //소켓 생성
        val socket = SocketClient()
        socket.connect(socket.serverIP,socket.port)

        //data 보내기
        var data = "${MainActivity.humity_in},${MainActivity.water_in}"
        socket.sendData(data)
        Log.d("SOCKET","\n[송신할 데이터]: $data")

        socket.closeConnect()
        Log.d("SOCKET","SocketSender 종료")
    }
}

class SocketClient : Serializable {
    private lateinit var socket: Socket
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var datainputStream: DataInputStream
    private lateinit var dataoutputStream: DataOutputStream

    val serverIP = "192.168.0.2" // 서버의 IP
    val port = 3022 // 서버의 port

    fun connect(ip: String,port: Int) {
        try {
            socket = Socket(ip, port)
            outputStream = socket.getOutputStream()
            inputStream = socket.getInputStream()
            dataoutputStream = DataOutputStream(outputStream)
            datainputStream = DataInputStream(inputStream)

        } catch (e: Exception) {
            Log.d("TEST","socket connect exception: $e")
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
        socket.close()
    }
}