package com.example.a2022_jarvis_android

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val helper = SqliteHelper(this,"logging",null,1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = RecyclerAdapter()
        adapter.listData.addAll(helper.select())
        adapter.helper = helper

        recyclerMemo.adapter = adapter
        recyclerMemo.layoutManager = LinearLayoutManager(this)

        save.setOnClickListener {
            val logging = Logging(null,editMemo.text.toString(),editMemo2.text.toString(),System.currentTimeMillis())
            helper.insert(logging)

            adapter.listData.clear()
            adapter.listData.addAll(helper.select())

            adapter.notifyDataSetChanged()
            editMemo.setText("")
            editMemo2.setText("")
        }
    }
}