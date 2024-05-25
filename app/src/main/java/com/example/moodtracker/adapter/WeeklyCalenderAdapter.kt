package com.example.moodtracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.R
import com.example.moodtracker.databinding.CalenderCellBinding
import com.example.moodtracker.interfaces.OnCellListener
import com.example.moodtracker.utils.CalenderUtils
import java.time.LocalDate

class WeeklyCalenderAdapter (private var daysOfWeek: List<LocalDate?>, private var listener: OnCellListener): RecyclerView.Adapter<WeeklyCalenderAdapter.WeeklyCalenderViewHolder>(){

    inner class WeeklyCalenderViewHolder(private val binding: CalenderCellBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cellText: String){
            binding.apply {
                cell.text = cellText
            }
        }

        fun setPresentCell(){
            binding.apply {
                parentView.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.grey))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeeklyCalenderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CalenderCellBinding.inflate(layoutInflater, parent,false)
        val layoutParams = binding.root.layoutParams
        layoutParams.height = parent.height
        return WeeklyCalenderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return daysOfWeek.size
    }

    override fun onBindViewHolder(holder: WeeklyCalenderViewHolder, position: Int) {
        val cellDate = daysOfWeek[position]
        if(cellDate == null){
            holder.bind("")
        } else{
            holder.bind(cellDate.dayOfMonth.toString())
            holder.itemView.setOnClickListener {
                listener.onMonthlyCellClick(cellDate)
            }
            if(cellDate == CalenderUtils.selectedDate)
                holder.setPresentCell()
        }
    }
}