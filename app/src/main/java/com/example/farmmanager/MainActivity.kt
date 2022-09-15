//메인엑티비티
package com.example.farmmanager



import androidx.appcompat.app.AppCompatActivity //MainActivity가 상속받을 클래스
import android.os.Bundle //MainActivity가 받을 자료형 클래스
import com.example.farmmanager.databinding.ActivityMainBinding //databing(레이아웃 연동)을 위한 클래스
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import java.io.*
import java.net.Socket


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
        Log.d("LOG_[MainActivity]","[MainActivity]  시작")

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

        SocketReceiver().start() //SocketReceiver 시작
    }
}

class SocketClient : Serializable {
    companion object{
        var originSocket: Socket? = null //소켓에 대한 변수

        lateinit var inputStream: InputStream //데이터를 받아오는 통로
        lateinit var datainputStream: DataInputStream //데이터를 받아오는 통로에 대한 래퍼

        lateinit var outputStream: OutputStream //데이터를 보낼 통로
        lateinit var dataoutputStream: DataOutputStream //데이터를 보낼 통로에 대한 래퍼

        var IsConnectRun = false

        //소켓 생성 및 연결
        fun connect() {
            Log.d("LOG_[SocketClient]","[connect] 시작")

            while(true){
                try {
                    IsConnectRun = true
                    originSocket = Socket(GlobalVariables.serverIP, GlobalVariables.serverPort) //서버의 IP, Port 를 바탕으로 소켓에 접속

                    inputStream = originSocket!!.getInputStream()
                    datainputStream = DataInputStream(inputStream)

                    outputStream = originSocket!!.getOutputStream()
                    dataoutputStream = DataOutputStream(outputStream)

                    Log.d("LOG_[SocketClient]","[connect] 성공")
                    //G.toast("[connect] 성공")
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

                    Thread.sleep(GlobalVariables.interval) //잠시 대기 후 재시작
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

        if(!SocketClient.IsConnectRun) SocketClient.connect() //소켓 생성 및 연결

        //data 받기 (습도,T이상,S이상)
        while(true){

            Log.d("LOG_[SocketReceiver]","[SocketReceiver] 실행 중")
            var recvData: String? = null //받아올 데이터를 저장할 변수
            while (recvData == null) {
                recvData = SocketClient.read()
                Log.d("LOG_[SocketReceiver]","[SocketReceiver] 수신된 데이터: $recvData")
            } //받아올 때까지 반복

            Log.d("LOG_[SocketReceiver]","[SocketReceiver] 수신된 데이터: $recvData")

            val tmp = recvData.split(",") // ,단위로 데이터를 분리

            GlobalVariables.humityReal = Integer.parseInt(tmp[0])
            GlobalVariables.tOdd = Integer.parseInt(tmp[1]) //수위탱크 상태 / 0: 정상, 1: 이상
            GlobalVariables.sOdd = Integer.parseInt(tmp[2]) //스프링쿨러 상태 / 0: 정상, 1: 이상

            sleep(GlobalVariables.interval) //잠시 대기 후 재실행
        }
    }
}



//데이터를 전송하는 스레드(단기)
class SocketSender : Thread() {

    override fun run() { //스레드 실행 시 동작하는 코드
        Log.d("LOG_[SocketSender]","[SocketSender] 시작")

        if(!SocketClient.IsConnectRun) SocketClient.connect() //소켓 생성 및 연결

        //data 전송(습도,경보,캡처)
        val data = "${GlobalVariables.humityTarget},${GlobalVariables.sirenOption},${GlobalVariables.motorOption},${GlobalVariables.captureOption}"
        SocketClient.sendData(data)
        Log.d("LOG_[SocketSender]","[SocketSender] 전송한 데이터: $data")
        //G.toast("[SocketSender] 전송 성공")

        Log.d("LOG_[SocketSender]","[SocketSender] 종료")
    }
}