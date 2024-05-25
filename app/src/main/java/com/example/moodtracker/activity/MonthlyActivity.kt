package com.example.moodtracker.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.R
import com.example.moodtracker.adapter.MonthlyCalenderAdapter
import com.example.moodtracker.interfaces.OnCellListener
import com.example.moodtracker.utils.CalenderUtils.daysInMonthArray
import com.example.moodtracker.utils.CalenderUtils.monthYearFromDate
import com.example.moodtracker.utils.CalenderUtils.selectedDate
import java.time.LocalDate

class MonthlyActivity : AppCompatActivity() , OnCellListener {
    private lateinit var monthlyCalenderRecyclerView: RecyclerView
    private lateinit var monthYearText: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monthy)
        initWidgets()
        setMonthView()
    }

    private fun initWidgets(){
        monthlyCalenderRecyclerView = findViewById(R.id.monthly_calender_recycler_view)
        monthYearText = findViewById(R.id.month_text)
    }

    private fun setMonthView() {
        monthYearText.text = monthYearFromDate(selectedDate)
        val daysInMonth = daysInMonthArray(selectedDate)
        val monthlyCalenderAdapter = MonthlyCalenderAdapter(daysInMonth, this)
        val layoutManager = GridLayoutManager(applicationContext, 7)
        monthlyCalenderRecyclerView.adapter = monthlyCalenderAdapter
        monthlyCalenderRecyclerView.layoutManager = layoutManager
    }

    override fun onMonthlyCellClick(cellDate: LocalDate){
        selectedDate = cellDate
        startActivity(Intent(this, WeeklyActivity::class.java))
    }

    fun previousMonthActivity(view: View){
        selectedDate = selectedDate.minusMonths(1);
        setMonthView()
    }

    fun nextMonthActivity(view: View){
        selectedDate = selectedDate.plusMonths(1);
        setMonthView()
    }

    fun weeklyAction(view: View) {
        startActivity(Intent(this, WeeklyActivity::class.java))
    }
}