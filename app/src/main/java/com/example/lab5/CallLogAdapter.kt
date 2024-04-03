package com.example.lab5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CallLogAdapter : RecyclerView.Adapter<CallLogAdapter.ViewHolder>() {

    private val callLogList = ArrayList<CallLogItem>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val number: TextView = view.findViewById(R.id.number)
        val date: TextView = view.findViewById(R.id.date)
        val duration: TextView = view.findViewById(R.id.duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_log_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val callLogItem = callLogList[position]
        holder.number.text = callLogItem.number
        holder.date.text = callLogItem.date
        holder.duration.text = callLogItem.duration
    }

    override fun getItemCount() = callLogList.size

    fun addCallLog(callLogItem: CallLogItem) {
        callLogList.add(callLogItem)
        notifyItemInserted(callLogList.size - 1)
    }
}