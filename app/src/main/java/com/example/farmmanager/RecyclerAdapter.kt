package com.example.farmmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.farmmanager.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var listData = ArrayList<Logging>()
    var helper:SqliteHelper? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val logging:Logging = listData.get(position)
        holder.setMemo(logging)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
    inner class Holder(val binding:ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        var mLogging:Logging? = null
        init{
            binding.btndel.setOnClickListener {
                helper?.delete(mLogging!!)
                listData.remove(mLogging)
                notifyDataSetChanged()
            }
        }
        fun setMemo(logging:Logging){
            binding.textid.text = logging.id.toString()
            binding.textContent2.text = logging.content2.toString()
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            binding.textDatetime.text = "${sdf.format(logging.datetime)}"

            this.mLogging = logging
        }
    }
}


