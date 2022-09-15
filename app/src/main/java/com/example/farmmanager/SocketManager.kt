//소켓과 관련된 클래스: SocketClient, SocketReceiver, SocketSender
package com.example.farmmanager



import android.util.Log
import java.io.*
import java.net.Socket

import com.example.farmmanager.GlobalVariables as G



class SocketClient : Serializable {
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
            Log.d("LOG_[SocketClient]","[connect] 시작")

            while(true){
                try {

                    Log.d("LOG_[SocketClient]","[connect] 연결 대기중")
                    originSocket = Socket(G.serverIP, G.serverPort) //서버의 IP, Port 를 바탕으로 소켓에 접속

                    inputStream = originSocket!!.getInputStream()
                    datainputStream = DataInputStream(inputStream)

                    outputStream = originSocket!!.getOutputStream()
                    dataoutputStream = DataOutputStream(outputStream)

                    Log.d("LOG_[SocketClient]","[connect] 성공")
                    G.toast("[connect] 성공")
                    IsConnected = true //연결이 되었음을 의미
                    break
                }

                catch (e: Exception) {
                    Log.d("LOG_[SocketClient]","[connect] $e")
                    if (originSocket == null){ //소켓이 생성되지 않았다면
                        Log.d("LOG_[SocketClient]","[connect] 소켓이 생성되지 않았습니다")
                        //G.toast("[connect] 소켓 생성 에러")
                    }

                    else{//소켓이 생성되었다면
                        originSocket?.close() //소켓을 닫기
                        Log.d("LOG_[SocketClient]","[connect] 에러: $e")
                        //G.toast("[connect] 에러")
                    }

                    Thread.sleep(G.interval) //잠시 대기 후 재시작
                    Log.d("LOG_[SocketClient]","[connect] 재시작")
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
                    Log.d("LOG_[SocketClient]","[sendData] 에러: $e")
                    closeConnect()
                    connect()
                }
            }

        }


        //전송된 데이터를 UTF-8 형식으로 디코딩하며 읽기
        fun read(): String? {
            while(true){
                try{
                    if (inputStream.available() > 0) {//데이터가 존재한다면
                        val msg = ByteArray(inputStream.available())
                        inputStream.read(msg)
                        return msg.toString(Charsets.UTF_8)
                    }
                    return null
//                    val msg = ByteArray(inputStream.available())
//                    inputStream.read(msg)
//                    return msg.toString(Charsets.UTF_8)
                }
                catch(e:java.lang.Exception){
                    Log.d("LOG_[SocketClient]","[read] 에러: $e")
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



//데이터를 받아오는 스레드(유지)
class SocketReceiver : Thread() {

    override fun run() { //스레드 실행 시 동작하는 코드
        Log.d("LOG_[SocketReceiver]","[SocketReceiver] 시작")



        //data 받기 (습도,T이상,S이상,모션)
        while(true){
            if(!SocketClient.IsConnectRun) SocketClient.connect() //소켓 생성 및 연결
            if(!SocketClient.IsConnected){
                Log.d("LOG_[SocketReceiver]","[SocketReceiver] connect 대기중..")
                G.toast("[SocketReceiver] 연결 대기 중.. 잠시 후에 시도해주세요")
            }
            else{
                Log.d("LOG_[SocketReceiver]","[SocketReceiver] 실행 중")
                var recvData: String? = null //받아올 데이터를 저장할 변수
                while (recvData == null) {
                    recvData = SocketClient.read()
                } //받아올 때까지 반복
                Log.d("LOG_[SocketReceiver]","[SocketReceiver] 수신된 데이터: $recvData")

                val tmp = recvData.split(",") // ,단위로 데이터를 분리

                G.humityReal = Integer.parseInt(tmp[0]) //실제 습도
                G.tOdd = Integer.parseInt(tmp[1]) //수위탱크 상태 / 0: 정상, 1: 이상
                G.sOdd = Integer.parseInt(tmp[2]) //스프링쿨러 상태 / 0: 정상, 1: 이상
                G.motionDetected = Integer.parseInt(tmp[3]) //모션 감지 / 0: 기본, 1: 감지
                if(G.motionDetected == 1) G.toastBell("[SocketSender] 동작이 감지되었습니다")
            }
            sleep(G.interval) //잠시 대기 후 재실행
        }
    }
}



//데이터를 전송하는 스레드(단기)
class SocketSender : Thread() {

    override fun run() { //스레드 실행 시 동작하는 코드
        Log.d("LOG_[SocketSender]","[SocketSender] 시작")
        G.isSend = false

        if(!SocketClient.IsConnectRun) SocketClient.connect() //소켓 생성 및 연결
        if(!SocketClient.IsConnected){
            Log.d("LOG_[SocketSender]","[SocketSender] connect 대기중..")
            G.toast("[SocketSender] 연결 대기 중.. 잠시 후에 시도해주세요")
        }
        else{
            //data 전송(습도,경보,캡처)
            val data = "${G.humityTarget},${G.sirenOption},${G.motorOption}"
            SocketClient.sendData(data)
            Log.d("LOG_[SocketSender]","[SocketSender] 전송한 데이터: $data")
            G.toast("[SocketSender] 전송 성공")
            G.isSend = false

            Log.d("LOG_[SocketSender]","[SocketSender] 종료")
        }
    }
}