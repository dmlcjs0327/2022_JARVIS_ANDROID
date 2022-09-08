//다이어리에 대한 엑티비티
package com.example.farmmanager



import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farmmanager.databinding.ActivityDiaryBinding



class DiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }

    //다이어리에 대한 sql helper & 리사이클러뷰에 대한 adapter
    val helper = DiaryDBHelper(this, "memo", null ,1)
    val adapter = DiaryDBRecyclerAdapter()


    //엑티비티 실행 시 동작코드
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //다이어리에 대한 sql helper & 리사이클러뷰에 대한 adapter 설정
        adapter.listData.addAll(helper.select())
        adapter.helper = helper

        binding.diarychart.adapter = adapter
        binding.diarychart.layoutManager = LinearLayoutManager(this)


        binding.btsave.setOnClickListener {
            if (binding.typingdiary.text.toString().isNotEmpty()) {
                val diarying = Diarying(null, binding.typingdiary.text.toString(), System.currentTimeMillis())
                helper.insert(diarying)
                adapter.listData.clear()
                adapter.listData.addAll(helper.select())
                adapter.notifyDataSetChanged()
                binding.typingdiary.setText("")
                Log.d("LOG_[DiaryActivity]","[DiaryActivity] 다이어리 저장")
                GlobalVariables.toast("[DiaryActivity] 다이어리 저장")
            }
        }

        binding.btout.setOnClickListener {
            finish()
        }
    }
}