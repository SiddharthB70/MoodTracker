package com.example.moodtracker.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.moodtracker.R
import com.example.moodtracker.database.EmotionDatabase
import com.example.moodtracker.utils.CalenderUtils
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatisticsActivity : AppCompatActivity() {
    private lateinit var emotionPieChart: PieChart
    private lateinit var weekBegin: LocalDate
    private lateinit var weekEnd: LocalDate
    private lateinit var specificEmotionPieChart: PieChart
    private lateinit var database: EmotionDatabase
    private lateinit var specificEmotionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        setWeeklyDate()
        database = EmotionDatabase.getInstance(this)

        emotionPieChart = findViewById(R.id.weekly_emotion_pie_chart)
        resetEmotionPieChart()
        setEmotionPieChart()

        specificEmotionTextView = findViewById(R.id.specific_emotion_text)
        specificEmotionPieChart = findViewById(R.id.specific_emotion_pie_chart)
        resetSpecificEmotionPieChart()

    }

    private fun resetSpecificEmotionPieChart() {
        val paint = specificEmotionPieChart.getPaint(PieChart.PAINT_INFO)
        paint.color = resources.getColor(R.color.white)
        paint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, resources.displayMetrics)
        paint.typeface = ResourcesCompat.getFont(this, R.font.roboto_mono)

        specificEmotionPieChart.legend.isEnabled = false
        specificEmotionPieChart.setHoleColor(resources.getColor(R.color.black))
        specificEmotionPieChart.setEntryLabelColor(resources.getColor(R.color.black))
        specificEmotionPieChart.setEntryLabelTypeface(ResourcesCompat.getFont(this, R.font.roboto_mono))
        specificEmotionPieChart.setUsePercentValues(true)
        specificEmotionPieChart.description.isEnabled = false
        specificEmotionPieChart.setTouchEnabled(false)
    }

    private fun setEmotionPieChart() {
        val entryList = mutableListOf<PieEntry>()
        val entryWithColorDao =  database.entryWithColorDao()
        lifecycleScope.launch (Dispatchers.IO){
            val emotionCountList = entryWithColorDao.getEmotionCount(weekBegin.toString(), weekEnd.toString())
            if(emotionCountList.isEmpty())
                return@launch
            emotionCountList.forEach{
                val pieEntry = PieEntry(it.count.toFloat(), it.emotion)
                entryList.add(pieEntry)
            }
            val colors = emotionCountList.map {
                Color.parseColor(it.color)
            }
            val pieDataSet = PieDataSet(entryList, "")
            pieDataSet.colors = colors
            val pieData = PieData(pieDataSet)
            pieData.setValueFormatter(PercentFormatter(emotionPieChart))
            pieData.setValueTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6f, resources.displayMetrics))
            withContext(Dispatchers.Main){
                emotionPieChart.data = pieData
                emotionPieChart.invalidate()
            }
            val onChartValueSelectedListener = object : OnChartValueSelectedListener{
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    val pieEntryLabel = (e as PieEntry).label
                    setSpecificEmotionPieChartCard(pieEntryLabel)
                }

                override fun onNothingSelected() {
                    specificEmotionTextView.text = "Specific Emotion Chart"
                    specificEmotionPieChart.clear()
                    specificEmotionPieChart.invalidate()
                }

            }
            emotionPieChart.setOnChartValueSelectedListener(onChartValueSelectedListener)
        }
    }

    private fun setSpecificEmotionPieChartCard(pieEntryLabel: String) {
        val emotionEntryDao = database.emotionEntryDao()
        val emotionColorDao = database.emotionColorDao()
        val entryList = mutableListOf<PieEntry>()

        specificEmotionTextView.text = "$pieEntryLabel Chart"
        lifecycleScope.launch (Dispatchers.IO){
            val noteEmotionCountList = emotionEntryDao.getNoteEmotionCount(weekBegin.toString(), weekEnd.toString(),pieEntryLabel)
            Log.i("list", noteEmotionCountList.toString())
            noteEmotionCountList.forEach {
                val entry = PieEntry(it.count.toFloat(), it.label)
                entryList.add(entry)
            }
            val color = emotionColorDao.getEmotionColor(pieEntryLabel)
            val pieChartColors: List<Int> = listOf(Color.parseColor(color.color))
            val pieDataSet = PieDataSet(entryList, "")
            pieDataSet.colors = pieChartColors
            val pieData = PieData(pieDataSet)
            pieData.setValueFormatter(PercentFormatter(specificEmotionPieChart))
            pieData.setValueTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 6f, resources.displayMetrics))
            withContext(Dispatchers.Main){
                specificEmotionPieChart.data = pieData
                specificEmotionPieChart.invalidate()
            }
        }
    }


    private fun resetEmotionPieChart() {
        val paint = emotionPieChart.getPaint(PieChart.PAINT_INFO)
        paint.color = resources.getColor(R.color.white)
        paint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20f, resources.displayMetrics)
        paint.typeface = ResourcesCompat.getFont(this, R.font.roboto_mono)

        emotionPieChart.legend.textColor = resources.getColor(R.color.white)
        emotionPieChart.legend.typeface = ResourcesCompat.getFont(this, R.font.roboto_mono)

        emotionPieChart.legend.isWordWrapEnabled = true
        emotionPieChart.setHoleColor(resources.getColor(R.color.black))
        emotionPieChart.setEntryLabelColor(resources.getColor(R.color.black))
        emotionPieChart.setEntryLabelTypeface(ResourcesCompat.getFont(this, R.font.roboto_mono))
        emotionPieChart.setUsePercentValues(true)
        emotionPieChart.description.isEnabled = false
    }

    private fun setWeeklyDate() {
        weekBegin = weekSunday()
        weekEnd = weekBegin.plusDays(6)
        val statisticsWeeklyView = findViewById<TextView>(R.id.statistics_weekly_date)
        val dateMonthYearFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val weekDate = weekBegin.format(dateMonthYearFormatter) + " - " + weekEnd.format(dateMonthYearFormatter)
        statisticsWeeklyView.text = weekDate
    }

    private fun weekSunday(): LocalDate{
        var currentDate = CalenderUtils.selectedDate
        val oneWeekBefore = currentDate.minusWeeks(1)
        while(currentDate.isAfter(oneWeekBefore)){
            if(currentDate.dayOfWeek == DayOfWeek.SUNDAY)
                return currentDate
            currentDate = currentDate.minusDays(1)
        }
        return currentDate
    }

}