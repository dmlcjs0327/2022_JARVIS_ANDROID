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

//메인 클래스
class MainActivity : AppCompatActivity() {

    //전역변수
    companion object{
        var humity_in = 40                                  // jw: 앱 안에서 설정한 값 _in  _08.25
        var humity_out = 80                                 // jw: 실제 값(센서가 측정한 값) _out  _08.25
        var water_in = 40
        var water_out = 80
        var alarm = false
    }

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val helper = SqliteHelper(this,"logging",null,1)


    override fun onCreate(savedInstanceState: Bundle?) {      // Run 시 가장 먼저 실행 _08.25
        super.onCreate(savedInstanceState)
        Log.d("[LOG]","MainActivity - onCreate: 시작")
        setContentView(binding.root) //setContentView에는 binding.root를 꼭 전달
        SocketReceiver().start() //SocketReceiver 시작

        val intent0 = Intent(this, BatActivity::class.java)
        val intent1 = Intent(this, CctvActivity::class.java)
        val intent2 = Intent(this, OptionActivity::class.java)
        val intent3 = Intent(this, diary::class.java)
        val adapter = RecyclerAdapter() //RecyclerAdpater 생성
        adapter.listData.addAll(helper.select()) //adapter의 listData에 DB에서 가져온 데이터를 세팅
        adapter.helper = helper
        binding.recyclerMemo.adapter = adapter //메인 화면의 리사이클러뷰 위젯에 adpater을 연결
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)  //레이아웃 매니저를 설정


        //레이아웃의 버튼들과 연동 => 클릭 시 해당 엑티비티 화면을 띄우기
        binding.btbat.setOnClickListener { startActivity(intent0) }
        binding.btCCTV.setOnClickListener { startActivity(intent1) }
        binding.btSetting.setOnClickListener { startActivity(intent2) }
        binding.btdiary.setOnClickListener { startActivity(intent3) }
    }
}

class SocketReceiver : Thread() {
    //Receiver는 계속 유지되어야 한다
    override fun run() {

        Log.d("LUC_TAG","SocketReceiver 시작")
        //소켓 생성

        try{SocketClient.connect()}
        catch(e:java.lang.Exception){ }

        Log.d("LUC_TAG","SocketReceiver 연결 완료")


        //data 받기 (밭습도,물높이)
        while(true){
            try{
                var recvData: String? = null
                while (recvData == null) recvData = SocketClient.read()         // jw: 라즈베리에서 주는 정보들을 read 하는 코드 _08.25
                Log.d("LUC_TAG","\n[수신된 데이터]: $recvData")

                var tmp = recvData.split(",")                          // jw: tmp 는 리스트단위로 저장됨 (why. split함수의 리턴값) _08.25
                MainActivity.humity_out = Integer.parseInt(tmp[0])
                MainActivity.water_out = Integer.parseInt(tmp[1])
                sleep(1000)

            }catch (e:java.lang.Exception){                                      // jw: 라즈베리에서 정보를 받아올 때 에러 시 catch _08.25
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
        while(true){                                                                // jw: 센더는 리시버가 연결 되어 있어야 주는 정보를 받을 수 있기 때문에 확인하는 필요문 _08.25
            try{
                if(SocketClient.socketBtn == false) SocketClient.connect()
                break
            }catch(e: java.lang.Exception){}
        }

        Log.d("LUC_TAG","SocketSender 연결 완료")

        while(true){                                                                // jw: 센더의 활용 시작 코드 _08.25
            try{
                //data 보내기
                var data = "${MainActivity.humity_in},${MainActivity.water_in}"     //jw: (tip) 전역변수를 사용하는 방법 : "클래스명.전역변수" _08.25
                SocketClient.sendData(data)                                         //jw: _in(앱에서 사용자가 설정한 값) 을 보내는 코드 _08.25
                Log.d("LUC_TAG","\n[송신할 데이터]: $data")

                SocketClient.closeConnect()                                         //jw: 값을 보내고, 연결된 소켓을 해제 하는것 ( 소켓을 닫는 것 ) _08.25 - 삭제 고려 중--
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

class SocketClient : Serializable {                        //jw: Serializable(직렬화) = 데이터 스트림을 사용 할 때 _08.25
    companion object{
        val serverIP = "192.168.0.2" // 서버의 IP           //jw: 의천이형 노트북 ip ㄸㄹㅅ, 라즈베리파이 ip 로 나중에 바꿔줘야함 _08.25
        val port = 3022 // 서버의 port                      //jw: 데이터를 받아 낼 포트( 사용하지 않는 포트를 랜덤으로 설정 한 것 ) _08.25


        lateinit var originSocket: Socket                   //jw: (tip) lateinit = 지연초기화 : 변수를 만들어 놓고 나중에 값을 할당 하겠다 _08.25
        lateinit var inputStream: InputStream               //jw: (tip) input과 output 의 stream 이 각각 있는 이유 : 받아오고 보내는 장치가 다르기때문에 _08.25
        lateinit var outputStream: OutputStream
        lateinit var datainputStream: DataInputStream       //jw: (tip) 명칭은 애매(래퍼스트림), 데이터 스트림을 다룰때, 더 편하며 안정적(?) 이기때문에 사용 _08.25
        lateinit var dataoutputStream: DataOutputStream

        var socketBtn = false                               //jw: 연결 여부를 확인하는 변수 [ true: 연결/ false: 연결되지 않음 ] _08.25

        fun connect() {                                             // jw: 소켓 생성과 (com)연결 + 데이터 스트림을 생성 _08.25
            try {
                originSocket = Socket(serverIP, port)               //jw: 서버의 ip 와 포트를 연결한 소켓을 만든다. _08.25
                outputStream = originSocket.getOutputStream()       //jw: (tip) 지연 초기화로 인해 초기화 해주는 과정 _08.25
                inputStream = originSocket.getInputStream()
                dataoutputStream = DataOutputStream(outputStream)
                datainputStream = DataInputStream(inputStream)
                socketBtn = true

            } catch (e: Exception) {                // jw: 예외 처리 후 다시 connet() 시도 _08.25 - 삭제 고려 중(why, connet() 를 사용할 때 마다 예외처리를 하고 있기 때문에)--
                Log.d("LUC_TAG","socket connect exception: $e")
                sleep(2000)
                Log.d("LUC_TAG","retry")
                connect()
            }
        }

        fun sendData(data: String) {                                    //jw: 받아온 데이터를 UTF-8 형식으로 디코딩 하여 전송 _08.25            And -> 라즈베리
//        outputStream.write(data.toByteArray(Charsets.UTF_8))
            dataoutputStream.writeUTF(data)                             //jw: (tip) 통로에 쓰기 = output = write (동급은 아니어도 비슷한 개념으로 이해) _08.25
        }

        fun read(): String? {                                               //jw: And <- 라즈베리 _08.25
            if (inputStream.available() > 0) {                              //jw: (tip) .available() > 0 이 문장은 데이터가 존재한다면(>0 이라면) 이라는 뜻 _08.25
//            return inputStream.bufferedReader(Charsets.UTF_8).toString()
                val msg = ByteArray(inputStream.available())                //jw: (tip) msg = 컨트롤러가 보내는 받아올 데이터, 이를 바이트배열의 크기로
                inputStream.read(msg)                                       //jw: (tip) 이미 스트림(통로) 를 통해 타고있는 데이터를 읽겠다는 것(And 가 읽겠다는 것)
                return msg.toString(Charsets.UTF_8)                         //jw: (tip) utf-8 형식으로 msg 를 바꿔서 리턴을 해줌
            }
            return null
        }

        fun closeConnect() {                                            //jw: 연결이 모두 끝났으면, 스트림과 소켓을 닫아주는 것이다
            outputStream.close()
            inputStream.close()
            originSocket.close()
        }
    }
}