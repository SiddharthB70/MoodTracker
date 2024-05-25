package com.example.moodtracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.databinding.LabelButtonBinding
import com.example.moodtracker.entity.Label
import com.example.moodtracker.fragment.ChangeEntryBottom

class LabelButtonAdapter(private var labelList: List<Label>, private var changeEntryBottom: ChangeEntryBottom): RecyclerView.Adapter<LabelButtonAdapter.LabelButtonViewHolder>() {
    inner class LabelButtonViewHolder(private val binding: LabelButtonBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(label: Label){
            binding.apply{
                labelButton.text = label.labelName
//                labelButton.background.alpha = 50
                labelButton.setOnClickListener {
                    changeEntryBottom.setBottomContainerNoteFragment(label.labelName)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelButtonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LabelButtonBinding.inflate(layoutInflater, parent, false)
        return LabelButtonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return labelList.size
    }

    override fun onBindViewHolder(holder: LabelButtonViewHolder, position: Int) {
        holder.bind(labelList[position])
    }

    fun setList(newList: List<Label>){
        labelList = newList
    }
}