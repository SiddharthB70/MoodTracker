package com.example.moodtracker.interfaces

import com.example.moodtracker.entity.EntryWithColor

interface OnEntryCellClick {
    fun onEntryCellClick(entryWithColor: EntryWithColor)
}