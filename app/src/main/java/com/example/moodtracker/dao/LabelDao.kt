package com.example.moodtracker.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moodtracker.entity.Label

@Dao
interface LabelDao {
    @Upsert
    fun addLabel(label: Label)

    @Query("SELECT * FROM label")
    fun getLabels(): List<Label>
}