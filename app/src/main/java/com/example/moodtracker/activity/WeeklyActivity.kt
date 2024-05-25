package com.example.moodtracker.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.R
import com.example.moodtracker.adapter.EntryAdapter
import com.example.moodtracker.adapter.WeeklyCalenderAdapter
import com.example.moodtracker.dao.EntryWithColorDao
import com.example.moodtracker.database.EmotionDatabase
import com.example.moodtracker.entity.EmotionEntryActivityData
import com.example.moodtracker.entity.EntryWithColor
import com.example.moodtracker.interfaces.OnCellListener
import com.example.moodtracker.interfaces.OnEntryCellClick
import com.example.moodtracker.utils.CalenderUtils
import com.example.moodtracker.utils.CalenderUtils.daysInWeekArray
import com.example.moodtracker.utils.CalenderUtils.monthYearFromDate
import com.example.moodtracker.utils.CalenderUtils.selectedDate
import com.example.moodtracker.utils.EntryPersistUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class WeeklyActivity : AppCompatActivity(), OnCellListener, OnEntryCellClick {
    private lateinit var weeklyDateView: RecyclerView
    private lateinit var montYearText: TextView
    private lateinit var entryWithColorDao: EntryWithColorDao
    private lateinit var entryRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly)
        initWidgets()
        val database = EmotionDatabase.getInstance(this@WeeklyActivity)
        entryWithColorDao = database.entryWithColorDao()
        setWeekView()

    }

    private fun initWidgets() {
        weeklyDateView = findViewById(R.id.weekly_date_view)
        val weeklyLayoutManager = GridLayoutManager(applicationContext, 7)
        weeklyDateView.layoutManager = weeklyLayoutManager

        montYearText = findViewById(R.id.month_text)

        entryRecyclerView = findViewById(R.id.entry_recycler_view)
        val entryLayoutManager = LinearLayoutManager(applicationContext)
        entryRecyclerView.layoutManager = entryLayoutManager
        val dividerItemDecoration = DividerItemDecoration(entryRecyclerView.context, entryLayoutManager.orientation)
        entryRecyclerView.addItemDecoration(dividerItemDecoration)

    }

    private fun setWeekView() {
        montYearText.text = monthYearFromDate(selectedDate)

        val days = daysInWeekArray(selectedDate)
        val weeklyCalenderAdapter = WeeklyCalenderAdapter(days, this)
        weeklyDateView.adapter = weeklyCalenderAdapter

        lifecycleScope.launch (Dispatchers.IO){
            val entryList = entryWithColorDao.getEntryWithColor(selectedDate.toString())
            val entryAdapter = EntryAdapter(entryList, this@WeeklyActivity)
            withContext(Dispatchers.Main){
                entryRecyclerView.adapter = entryAdapter
            }
        }

    }


    fun previousWeek(view: View) {
        selectedDate = selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeek(view: View) {
        selectedDate = selectedDate.plusWeeks(1)
        setWeekView()
    }

    fun monthlyAction(view: View) {
        startActivity(Intent(this, MonthlyActivity::class.java))
    }

    override fun onMonthlyCellClick(cellDate: LocalDate) {
        selectedDate = cellDate
        setWeekView()
    }

    override fun onEntryCellClick(entryWithColor: EntryWithColor) {
//        val toast = Toast.makeText(this, entryWithColor.toString(), Toast.LENGTH_SHORT)
//        toast.show()
        val gson = Gson()
        val entryWithColorString = gson.toJson(entryWithColor)
        val intent = Intent(this, CompleteEntryActivity::class.java)
        intent.putExtra("entryWithColorString", entryWithColorString)
        startActivity(intent)
    }

    fun newDiaryEntry(view: View) {
        val entry = EmotionEntryActivityData()
        val presentDate = LocalDate.now()
        EntryPersistUtil.setEntry(entry)
        if(selectedDate == presentDate){
            entry.date = presentDate
            startActivity(Intent(this, DiaryEntryActivity::class.java))
        } else{
            val dateDialog = Dialog(this@WeeklyActivity, R.style.DialogTheme)
            dateDialog.setContentView(R.layout.date_dialog )
            val presentDateButton = dateDialog.findViewById<Button>(R.id.present_day_button)
            val selectedDateButton = dateDialog.findViewById<Button>(R.id.selected_date_button)
            selectedDateButton.text = CalenderUtils.dayMonthYearFromDate(selectedDate)

            presentDateButton.setOnClickListener {
                setDate(LocalDate.now())
            }

            selectedDateButton.setOnClickListener {
                setDate(selectedDate)
            }
            dateDialog.show()
        }
    }

    private fun setDate(date: LocalDate){
        EntryPersistUtil.getEntry().date = date
        startActivity(Intent(this, DiaryEntryActivity::class.java))
    }

    fun showWeeklyStatistics(view: View) {
        startActivity(Intent(this@WeeklyActivity, StatisticsActivity::class.java ))
    }

}