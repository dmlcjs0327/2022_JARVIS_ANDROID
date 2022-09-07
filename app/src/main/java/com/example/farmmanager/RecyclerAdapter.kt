package com.example.farmmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.farmmanager.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

// 리사이클러뷰는 리사이클러뷰어댑터라는 메서드 어댑터를 사용해서 데이터 연결해야함
class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var listData = ArrayList<Logging>()  //어댑터에서 사용할 데이터 목록 변수
    var helper:MainDBHelper? = null

    //한 화면에 그려지는 아이템 개수만큼 레이아웃 생성 ex)한화면에 6줄이 보이면 6번 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding) //생성된 binding을 Holder 클래스에 담아서 반환
    }

    override fun getItemCount(): Int {  //목록에 보여줄 아이템 개수
        return listData.size//리사이클러뷰에서 사용할 데이터의 총개수 리턴
    }

    override fun onBindViewHolder(holder: Holder, position: Int) { //생성된 아이템 레이아웃에 값 입력후 목록(화면)에 출력
        val logging:Logging = listData.get(position) //listData에 있는 logging형태로 저장된 데이터둘 중 현재 위치에서 하나 꺼내 logging변수에 저장
        holder.setMemo(logging) //Holder 클래스의 setMemo 메서드에 데이터 전달
    }


    inner class Holder(val binding:ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        //이 Holder class는 화면에 보여지는 개수 만큼만 뷰홀더를 생성하고 목록을 위로 스크롤 할 경우 가장 위의 뷰홀더를 가장 아래 뷰 홀더에서 가져와 재사용한 후 데이터만 바꿔주는 역할을
        //하기때문에 앱의 효율을 상승시킨다. - ViewHolder클래스 상속받음 (어댑터에서 바인딩을 생성한 후에 뷰 홀더에 넘겨줌)
        var mLogging:Logging? = null //클릭하는시점에 어떤 데이터를 삭제할 것인지 위치를 알기위한 변수
        init{
            binding.btndel.setOnClickListener { //삭제버튼을 누르면 helper와 listData에 접근하여 삭제하고 어댑터 갱신
                helper?.delete(mLogging!!)
                listData.remove(mLogging)
                notifyDataSetChanged()
            }
        }
        fun setMemo(logging:Logging){ //화면에 데이터를 세팅
            binding.textid.text = "${logging.id}"
            binding.textContent2.text = logging.content2
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            binding.textDatetime.text = "${sdf.format(logging.datetime)}"
            this.mLogging = logging
        }
    }
}


