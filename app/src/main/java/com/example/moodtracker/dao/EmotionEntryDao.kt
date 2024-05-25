package com.example.moodtracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.moodtracker.entity.EmotionEntry
import com.example.moodtracker.entity.NoteEmotionCount

@Dao
interface EmotionEntryDao {
    @Upsert
    fun insertEmotionEntry(entry:EmotionEntry)

    @Delete
    fun deleteEmotionEntry(entry:EmotionEntry)

    @Query("SELECT label, Count(label) AS count FROM emotion_entry WHERE Date(date) BETWEEN :weekBegin AND :weekEnd AND emotion=:emotion GROUP BY label, emotion")
    fun getNoteEmotionCount(weekBegin: String, weekEnd: String, emotion:String): MutableList<NoteEmotionCount>
}