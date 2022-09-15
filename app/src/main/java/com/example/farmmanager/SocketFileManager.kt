//소켓과 관련된 클래스: SocketFileClient, SocketFileReceiver, SocketFileSender
package com.example.farmmanager



import android.util.Log
import java.io.*
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

import com.example.farmmanager.GlobalVariables as G



class SocketFileClient : Serializable {
    companion object{
        var originSocket: Socket? = null //소켓에 대한 변수

        lateinit var inputStream: InputStream //데이터를 받아오는 통로
        lateinit var datainputStream: DataInputStream //데이터를 받아오는 통로에 대한 래퍼

        lateinit var outputStream: OutputStream //데이터를 보낼 통로
        lateinit var dataoutputStream: DataOutputStream //데이터를 보낼 통로에 대한 래퍼

        var IsConnectRun = false
        var IsConnected = false

        //소켓 생성 및 연결
        fun connect() {
            IsConnectRun = true //connect 실행 중을 의미
            IsConnected = false //아직 연결이 안 되었음을 의미
            Log.d("LOG_[SocketFileClient]","[connect] 시작")

            while(true){
                try {

                    Log.d("LOG_[SocketFileClient]","[connect] 연결 대기중")
                    originSocket = Socket(G.serverIP, G.serverFilePort) //서버의 IP, Port 를 바탕으로 소켓에 접속

                    inputStream = originSocket!!.getInputStream()
                    datainputStream = DataInputStream(inputStream)

                    outputStream = originSocket!!.getOutputStream()
                    dataoutputStream = DataOutputStream(outputStream)

                    Log.d("LOG_[SocketFileClient]","[connect] 성공")
                    G.toast("[connect] 성공")
                    IsConnected = true //연결이 되었음을 의미
                    break
                }

                catch (e: Exception) {
                    Log.d("LOG_[SocketFileClient]","[connect] $e")
                    if (originSocket == null){ //소켓이 생성되지 않았다면
                        Log.d("LOG_[SocketFileClient]","[connect] 소켓이 생성되지 않았습니다")
                        //G.toast("[connect] 소켓 생성 에러")
                    }

                    else{//소켓이 생성되었다면
                        originSocket?.close() //소켓을 닫기
                        Log.d("LOG_[SocketFileClient]","[connect] 에러: $e")
                        //G.toast("[connect] 에러")
                    }

                    Thread.sleep(G.interval) //잠시 대기 후 재시작
                    Log.d("LOG_[SocketFileClient]","[connect] 재시작")
                    //G.toast("[connect] 재시작")
                }
            }
        }


        //data를 UTF-8 형식으로 인코딩하여 전송
        fun sendData(data: String) {
            while(true){
                try{
                    dataoutputStream.writeUTF(data)
                    return
                }

                catch(e:java.lang.Exception){
                    Log.d("LOG_[SocketFileClient]","[sendData] 에러: $e")
                    closeConnect()
                    connect()
                }
            }

        }


        //전송된 데이터를 UTF-8 형식으로 디코딩하며 읽기
        fun read(): ByteArray? {
            while(true){
                try{
                    if (inputStream.available() > 0) {//데이터가 존재한다면
                        val msg = ByteArray(inputStream.available())
                        inputStream.read(msg)
                        return msg
                    }
                    return null
//                    val msg = ByteArray(inputStream.available())
//                    inputStream.read(msg)
//                    return msg.toString(Charsets.UTF_8)
                }
                catch(e:java.lang.Exception){
                    Log.d("LOG_[SocketFileClient]","[read] 에러: $e")
                    closeConnect()
                    connect()
                }
            }
        }


        //소켓을 종료
        fun closeConnect() {
            outputStream.close()
            inputStream.close()
            originSocket?.close()
        }
    }
}



//데이터를 전송하는 스레드(단기)
class SocketFileSender : Thread() {

    override fun run() { //스레드 실행 시 동작하는 코드
        Log.d("LOG_[SocketFileSender]","[SocketFileSender] 시작")

        if(!SocketFileClient.IsConnectRun) SocketFileClient.connect() //소켓 생성 및 연결
        if(!SocketFileClient.IsConnected){
            Log.d("LOG_[SocketFileSender]","[SocketFileSender] connect 대기중..")
            G.toast("[SocketFileSender] 연결 대기 중.. 잠시 후에 시도해주세요")
        }
        else{
            //data 전송
            SocketFileClient.sendData("start")
            Log.d("LOG[SocketFileReceiver]","[SocketFileReceiver] 사진 받기 시작")

            val dir = File(G.directory)
            if(!dir.exists()) {
                dir.mkdirs()
            }

            val curTime = SimpleDateFormat("yyyy_MM_dd_hh_mm", Locale.getDefault()).format(Calendar.getInstance().time)
            val writer = FileWriter(G.directory +curTime+".png") //쓸 파일
            val buffer = BufferedWriter(writer) //쓸 버퍼

            var recvData: String? = null //받아올 데이터를 저장할 변수
            while (recvData == null) {
                recvData = SocketFileClient.read().toString()
                buffer.write(recvData)
            } //받아올 때까지 반복
            buffer.close() //버퍼닫기
        }
    }
}