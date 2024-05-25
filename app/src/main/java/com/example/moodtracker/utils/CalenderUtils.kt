package com.example.moodtracker.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

object CalenderUtils {
    var selectedDate: LocalDate = LocalDate.now()

    fun monthYearFromDate(date: LocalDate):String{
        val formatter = DateTimeFormatter.ofPattern("MMMM, yyyy")
        return date.format(formatter)
    }

    fun dayMonthYearFromDate(date: LocalDate): String{
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
        return date.format(formatter)
    }

    fun daysInMonthArray(date: LocalDate): List<LocalDate?> {
        val daysInMonthArray = mutableListOf<LocalDate?>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        for(i in 1..42){
            if(i<=dayOfWeek || i>daysInMonth+dayOfWeek)
                daysInMonthArray.add(null)
            else
                daysInMonthArray.add(LocalDate.of(date.year,date.month,i - dayOfWeek))
        }
        return daysInMonthArray
    }

    fun daysInWeekArray(date: LocalDate): List<LocalDate>{
        val days = mutableListOf<LocalDate>()
        var currentDate = sundayForDate(date)
        val endDate = currentDate.plusWeeks(1)
        while(currentDate.isBefore(endDate)){
            days.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
        return days
    }

    private fun sundayForDate(date: LocalDate): LocalDate {
        var currentDate = date
        val oneWeekAgo = currentDate.minusWeeks(1)
        while (currentDate.isAfter(oneWeekAgo))
        {
            if(currentDate.dayOfWeek == DayOfWeek.SUNDAY)
                return currentDate

            currentDate = currentDate.minusDays(1)
        }

        return currentDate
    }


}