package com.example.moodtracker.entity

import java.time.LocalDate

data class EmotionEntryActivityData(
    var date:LocalDate = LocalDate.now(),
    var emotion: String = "",
    var color: String = "",
    var label: String = "",
    var note: String = ""
)
