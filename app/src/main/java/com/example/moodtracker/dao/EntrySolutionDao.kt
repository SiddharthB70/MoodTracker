package com.example.moodtracker.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.moodtracker.entity.EntrySolution

@Dao
interface EntrySolutionDao {
    @Upsert
    fun insertSolution(entrySolution: EntrySolution)

    @Query("Select * from entry_solution where entryId=:entryId")
    fun getSolution(entryId:Int): EntrySolution?

    @Delete
    fun deleteSolution(entrySolution: EntrySolution)
}