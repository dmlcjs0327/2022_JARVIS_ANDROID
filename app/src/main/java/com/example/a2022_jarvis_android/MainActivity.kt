package com.example.a2022_jarvis_android

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a2022_jarvis_android.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    val helper = SqliteHelper(this,"logging",null,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root);

        val adapter = RecyclerAdapter()
        adapter.listData.addAll(helper.select())
        adapter.helper = helper

        binding.recyclerMemo.adapter = adapter
        binding.recyclerMemo.layoutManager = LinearLayoutManager(this)

        binding.save.setOnClickListener {
            val logging = Logging(null,binding.editMemo.text.toString(),binding.editMemo2.text.toString(),System.currentTimeMillis())
            helper.insert(logging)

            adapter.listData.clear()
            adapter.listData.addAll(helper.select())

            adapter.notifyDataSetChanged()
            binding.editMemo.setText("")
            binding.editMemo2.setText("")
        }
    }
}