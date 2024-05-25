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

class MonthlyCalenderAdapter(private var daysOfMonth: List<LocalDate?>, private val listener: OnCellListener):RecyclerView.Adapter<MonthlyCalenderAdapter.MonthlyCalenderViewHolder>() {

    inner class MonthlyCalenderViewHolder(private val binding: CalenderCellBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(cellText: String) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthlyCalenderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CalenderCellBinding.inflate(layoutInflater, parent,false)
        val layoutParams = binding.root.layoutParams
        layoutParams.height = (parent.height*0.16666666).toInt()
        return MonthlyCalenderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return daysOfMonth.size
    }

    override fun onBindViewHolder(holder: MonthlyCalenderViewHolder, position: Int) {
        val cellDate = daysOfMonth[position]
        if(cellDate == null){
            holder.bind("")
        } else{
            holder.bind(cellDate.dayOfMonth.toString())
            holder.itemView.setOnClickListener{
                listener.onMonthlyCellClick(cellDate)
            }
            if(cellDate == CalenderUtils.selectedDate)
                holder.setPresentCell()

        }

    }

}