package com.example.moodtracker.interfaces

import java.time.LocalDate

interface OnCellListener {
    fun onMonthlyCellClick(cellDate: LocalDate)
}