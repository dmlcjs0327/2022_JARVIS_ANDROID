package com.example.a2022_jarvis_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a2022_jarvis_android.databinding.ActivityMainBinding
import com.example.a2022_jarvis_android.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var listData = ArrayList<Logging>()
    var helper:SqliteHelper? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding).apply {
            binding.btndel.setOnClickListener {
                var cursor = adapterPosition

                helper?.delete(listData.get(cursor))
                listData.remove(listData.get(cursor))

                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val logging:Logging = listData.get(position)
        holder.setMemo(logging)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class Holder(val binding:ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setMemo(logging:Logging){
            binding.textId.text = logging.id.toString()
            binding.textContent.text= logging.content.toString()
            binding.textContent2.text = logging.content2.toString()
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            binding.textDatetime.text = "${sdf.format(logging.datetime)}"
        }
    }
}

