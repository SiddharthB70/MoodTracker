package com.example.moodtracker.utils

import com.example.moodtracker.entity.EmotionEntryActivityData

object EntryPersistUtil {
    private lateinit var entry: EmotionEntryActivityData

    fun setEntry(givenEntry: EmotionEntryActivityData){
        entry = givenEntry
    }

    fun getEntry(): EmotionEntryActivityData{
        return entry
    }
}