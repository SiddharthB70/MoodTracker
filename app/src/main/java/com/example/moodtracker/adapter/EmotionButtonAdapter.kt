package com.example.moodtracker.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.databinding.EmotionButtonBinding
import com.example.moodtracker.entity.EmotionColor
import com.example.moodtracker.fragment.ChangeEntryBottom

class EmotionButtonAdapter(
    private var emotionColors: List<EmotionColor>,
    private var changeEntryBottom: ChangeEntryBottom
):RecyclerView.Adapter<EmotionButtonAdapter.EmotionButtonViewHolder>(){
   inner class EmotionButtonViewHolder(private val binding: EmotionButtonBinding): RecyclerView.ViewHolder(binding.root){
       fun bind(emotionColor: EmotionColor){
           binding.apply{
               emotionButton.text = emotionColor.emotion
               ViewCompat.setBackgroundTintList(emotionButton, ColorStateList.valueOf(Color.parseColor(emotionColor.color)))
               emotionButton.setOnClickListener{
                   changeEntryBottom.setBottomContainerLabelFragment(emotionColor.emotion, emotionColor.color)
               }
           }
       }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmotionButtonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EmotionButtonBinding.inflate(layoutInflater, parent, false)
        return EmotionButtonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return emotionColors.size
    }

    override fun onBindViewHolder(holder: EmotionButtonViewHolder, position: Int) {
        holder.bind(emotionColors[position])
    }

}