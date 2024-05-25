package com.example.moodtracker.fragment

import java.time.LocalTime

interface SaveEntry {
    fun saveEntry(time: LocalTime)
}