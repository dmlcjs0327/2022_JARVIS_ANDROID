package com.example.farmmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.farmmanager.databinding.ActivityDiaryBinding



class DiaryActivity : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    val helper = DiaryDBHelper(this, "memo", null ,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = diarychart()
        adapter.helper = helper
        adapter.listData.addAll(helper.selectMemo())
        binding.diarychart.adapter = adapter
        binding.diarychart.layoutManager = LinearLayoutManager(this)

        binding.btsave.setOnClickListener {
            if (binding.typingdiary.text.toString().isNotEmpty()) {
                val diarying = Diarying(null, binding.typingdiary.text.toString(), System.currentTimeMillis())

                helper.insertMemo(diarying)

                adapter.listData.clear()
                adapter.listData.addAll(helper.selectMemo())
                adapter.notifyDataSetChanged()
                binding.typingdiary.setText("")
            }
        }

        binding.btout.setOnClickListener {
            finish()
        }
    }
}