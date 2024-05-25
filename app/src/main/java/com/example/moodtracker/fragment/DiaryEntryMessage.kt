package com.example.moodtracker.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.moodtracker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DiaryEntryMessage : Fragment(R.layout.fragment_diary_entry_message) {

    private lateinit var messageBoard:TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageBoard = view.findViewById(R.id.messageBoard)
    }

    fun typeWrite(lifecycleOwner: LifecycleOwner, text:String){
        lifecycleOwner.lifecycleScope.launch {
            repeat(text.length){
                delay(20)
                messageBoard.text = messageBoard.text.toString() + text[it]
            }
        }
    }
}