package com.example.moodtracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.moodtracker.databinding.DailyDiaryEntryCellBinding
import com.example.moodtracker.entity.EntryWithColor
import com.example.moodtracker.interfaces.OnEntryCellClick

class EntryAdapter(private val entryList:List<EntryWithColor>, private val listener: OnEntryCellClick): Adapter<EntryAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(private val binding: DailyDiaryEntryCellBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(entryWithColor: EntryWithColor){
            binding.apply{
                emotionColorCard.setBackgroundColor(Color.parseColor(entryWithColor.emotionColor.color))
                emotionLabelView.text = entryWithColor.emotionEntry.label
                emotionTimeView.text = entryWithColor.emotionEntry.time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DailyDiaryEntryCellBinding.inflate(layoutInflater,parent,false)
        return EntryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entryList[position])
        holder.itemView.setOnClickListener {
            listener.onEntryCellClick(entryList[position])
        }
    }
}