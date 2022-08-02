package com.example.a2022_jarvis_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_recycler.view.*
import java.text.SimpleDateFormat

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var listData = ArrayList<Logging>()
    var helper:SqliteHelper? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler,parent,false)
        return Holder(view).apply {
            itemView.btn_del.setOnClickListener {
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


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setMemo(logging:Logging){
            itemView.textId.text = logging.id.toString()
            itemView.textContent.text= logging.content.toString()
            itemView.textContent2.text = logging.content2.toString()
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            itemView.textDatetime.text = "${sdf.format(logging.datetime)}"

        }

    }

}

