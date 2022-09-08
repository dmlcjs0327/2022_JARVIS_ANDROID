package com.example.farmmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.farmmanager.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 리사이클러뷰는 리사이클러뷰어댑터라는 메서드 어댑터를 사용해서 데이터 연결해야함
class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var listData = ArrayList<Logging>()  //어댑터에서 사용할 데이터 목록 변수
    var helper:MainDBHelper? = null //현 프로그램의 전반적인 DB를 관리하기 위한 클래스


    //한 화면에 그려지는 아이템 개수만큼 레이아웃 생성 ex)한화면에 6줄이 보이면 6번 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding) //생성된 binding을 Holder 클래스에 담아서 반환
    }


    //목록에 보여줄 아이템 개수
    override fun getItemCount(): Int {
        return listData.size//리사이클러뷰에서 사용할 데이터의 총개수 리턴
    }


    //생성된 아이템 레이아웃에 값 입력후 목록(화면)에 출력
    override fun onBindViewHolder(holder: Holder, index: Int) {
        holder.setMemo(listData[index]) //Holder 클래스의 setMemo 메서드에 데이터 전달
    }


    inner class Holder(val binding:ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        //Holder: 화면에 보여지는 개수 만큼만 뷰홀더를 생성 (ViewHolder클래스 상속받으며, 어댑터에서 바인딩을 생성한 후에 뷰 홀더에 넘겨줌)
        //        목록을 위로 스크롤 할 경우 가장 위의 뷰홀더를 가장 아래 뷰 홀더에서 가져와 데이터만 바꿔주는 역할
        //        =>앱의 효율을 상승
        var mLogging:Logging? = null //클릭하는시점에 어떤 데이터를 삭제할 것인지 위치를 알기위한 변수


        //생성자: 삭제버튼을 누르면 helper와 listData에 접근하여 삭제하고 어댑터 갱신
        init{
            binding.btndel.setOnClickListener {
                helper?.delete(mLogging!!) //DB에서 mLogging
                listData.remove(mLogging)
                notifyDataSetChanged() //리스트 업데이트
            }
        }


        //화면에 데이터를 세팅
        fun setMemo(logging:Logging){
            binding.textid.text = "${logging.id}"
            binding.textContent2.text = logging.content
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm") //현재 시간
            binding.textDatetime.text = "${sdf.format(logging.datetime)}" //뷰에서 시간 text를 수정
            this.mLogging = logging
        }
    }
}


