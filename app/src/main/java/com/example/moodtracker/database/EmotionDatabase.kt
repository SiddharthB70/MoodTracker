package com.example.moodtracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moodtracker.dao.EmotionColorDao
import com.example.moodtracker.dao.EmotionEntryDao
import com.example.moodtracker.dao.EntrySolutionDao
import com.example.moodtracker.dao.EntryWithColorDao
import com.example.moodtracker.dao.LabelDao
import com.example.moodtracker.entity.EmotionColor
import com.example.moodtracker.entity.EmotionEntry
import com.example.moodtracker.entity.EntrySolution
import com.example.moodtracker.entity.Label

@Database(
    entities = [EmotionColor::class, EmotionEntry::class, Label::class, EntrySolution::class],
    version=1
)
abstract class EmotionDatabase:RoomDatabase() {
    abstract fun emotionColorDao(): EmotionColorDao
    abstract fun emotionEntryDao(): EmotionEntryDao
    abstract fun labelDao(): LabelDao
    abstract fun entryWithColorDao(): EntryWithColorDao
    abstract fun entrySolutionDao(): EntrySolutionDao

    companion object {
        private var INSTANCE: EmotionDatabase? = null
        fun getInstance(context: Context): EmotionDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context.applicationContext, EmotionDatabase::class.java, "mood_tracker_database")
                            .createFromAsset("database/moodTracker.db")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}