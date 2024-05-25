package com.example.moodtracker.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.R
import com.example.moodtracker.adapter.EmotionButtonAdapter
import com.example.moodtracker.database.EmotionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiaryEntryEmotion : Fragment(R.layout.fragment_diary_entry_emotion) {
    private lateinit var changeEntryBottom: ChangeEntryBottom;
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = EmotionDatabase.getInstance(view.context)
        val emotionColorDao = database.emotionColorDao()
        val emotionButtonRecyclerView = view.findViewById<RecyclerView>(R.id.button_recycler_view)
        lifecycleScope.launch (Dispatchers.IO){
            val emotionColors = emotionColorDao.getEmotionColor()
            withContext(Dispatchers.Main){
                val layoutManager = GridLayoutManager(view.context, 2)
                val emotionButtonAdapter = EmotionButtonAdapter(emotionColors, changeEntryBottom )
                emotionButtonRecyclerView.adapter =emotionButtonAdapter
                emotionButtonRecyclerView.layoutManager = layoutManager
            }
        }
    }

    fun setChangeEntryBottom(changeEntryBottom: ChangeEntryBottom){
        this.changeEntryBottom = changeEntryBottom
    }
}