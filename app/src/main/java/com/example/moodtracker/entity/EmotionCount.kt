package com.example.moodtracker.entity

import androidx.room.ColumnInfo

data class EmotionCount(
    @ColumnInfo("emotion")val emotion: String,
    @ColumnInfo("count") val count:Int,
    @ColumnInfo("color")val color:String
)
