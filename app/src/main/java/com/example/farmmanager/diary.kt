package com.example.farmmanager

import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.VERSION_CODES.S
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.farmmanager.databinding.ActivityDiaryBinding

class diary : AppCompatActivity() {

    val binding by lazy { ActivityDiaryBinding.inflate(layoutInflater) }
    val helper = SqliteHelper(this, "memo", 1)

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
                val memo = Memo(null, binding.typingdiary.text.toString(), System.currentTimeMillis())

                helper.insertMemo(memo)

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