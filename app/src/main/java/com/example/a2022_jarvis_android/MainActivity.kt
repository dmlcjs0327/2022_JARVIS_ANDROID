package com.example.a2022_jarvis_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.*
import java.net.Socket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendMsgBtn: Button = findViewById(R.id.send_button)
        sendMsgBtn.setOnClickListener {
            ClientThread().start()
        }
    }

    class ClientThread : Thread() {
        override fun run() {

            val serverIP = "192.168.0.2" // 서버의 IP
            val port = 3022
            val data = "\nThis is the client message.\n이것은 클라이언트 메세지입니다.\n <by android>"

            val socket = SocketClient()
            socket.connect(serverIP,port)

            //data 보내기
            socket.sendData(data)
            Log.d("TEST","\n[송신할 데이터]: $data")

            //data 받기
            var recvData: String? = null
            while (recvData == null) recvData = socket.read()
            Log.d("TEST","\n[수신된 데이터]: $recvData")
            socket.closeConnect()
        }
    }
}

class SocketClient : Serializable {
    private lateinit var socket: Socket
    private lateinit var inputStream: InputStream
    private lateinit var outputStream: OutputStream
    private lateinit var datainputStream: DataInputStream
    private lateinit var dataoutputStream: DataOutputStream

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