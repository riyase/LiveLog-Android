package com.riyase.livelog.views.log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.riyase.livelog.R

class LogAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var multiline = false
    val list = ArrayList<String>()

    fun addLog(log : String) {
        list.add(log)
        notifyDataSetChanged()
    }

    fun toggleMultiLine() : Boolean {
        multiline = !multiline
        notifyDataSetChanged()
        return multiline
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            VHItemSingle(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.list_item_log_single,
                        parent,
                        false
                    )
            )
        } else {
            VHItemMulti(
                LayoutInflater.from(parent.context)
                    .inflate(
                        R.layout.list_item_log_multi,
                        parent,
                        false
                    )
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun getItemViewType(position: Int): Int {
        return if (multiline) { 1 } else { 0 }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (multiline) {
            (holder as VHItemMulti).bind(list.get(position))
        } else {
            (holder as VHItemSingle).bind(list.get(position))
        }
    }

    class VHItemSingle(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView : TextView = itemView.findViewById(R.id.textView)

        fun bind(log : String) {
            textView.text = log
        }
    }

    class VHItemMulti(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView : TextView = itemView.findViewById(R.id.textView)

        fun bind(log : String) {
            textView.text = log
        }
    }
}