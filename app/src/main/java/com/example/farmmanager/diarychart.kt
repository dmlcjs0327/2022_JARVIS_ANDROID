package com.example.farmmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.farmmanager.databinding.ItemDiarychartBinding

class diarychart: RecyclerView.Adapter<diarychart.Holder>() {
    var listData = mutableListOf<Memo>()
    var helper: SqliteHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDiarychartBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }

    inner class Holder(val binding: ItemDiarychartBinding): RecyclerView.ViewHolder(binding.root) {
        var mMemo: Memo? = null
        init {
            binding.btndelete.setOnClickListener {
                helper?.deleteMemo(mMemo!!)
                listData.remove(mMemo)
                notifyDataSetChanged()
            }
        }
        fun setMemo(memo: Memo) {
            binding.recordnum.text = "${memo.no}"
            binding.record.text = memo.content
            val sdf = java.text.SimpleDateFormat("yyyy/MM/dd hh:mm")
            binding.recordDate.text = "${sdf.format(memo.datetime)}"

            this.mMemo = memo
        }
    }
}