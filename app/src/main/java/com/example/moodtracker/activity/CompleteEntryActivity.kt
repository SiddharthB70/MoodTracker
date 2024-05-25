package com.example.moodtracker.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.moodtracker.R
import com.example.moodtracker.database.EmotionDatabase
import com.example.moodtracker.entity.EntrySolution
import com.example.moodtracker.entity.EntryWithColor
import com.example.moodtracker.entity.LLMResponse
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class CompleteEntryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_entry)
        val gson = Gson()
        val entryWithColor = gson.fromJson<EntryWithColor>(intent.getStringExtra("entryWithColorString"),EntryWithColor::class.java)
        setCompleteEntry(entryWithColor)
        setCompleteEntrySolution(entryWithColor)
    }

    private fun setCompleteEntrySolution(entryWithColor: EntryWithColor) {
        val solutionText = findViewById<TextView>(R.id.entry_solution_view)
        val database = EmotionDatabase.getInstance(this@CompleteEntryActivity)
        val entrySolutionDao = database.entrySolutionDao()
        val client = HttpClient(CIO){
            install(HttpTimeout){
                requestTimeoutMillis = 120000
            }
            install(ContentNegotiation){
                gson()
            }
        }

        val sharedPref = getSharedPreferences("usernamePref", Context.MODE_PRIVATE)
        val storedUsername: String? = sharedPref.getString("username","")
        val noteJsonObject = JSONObject()

        noteJsonObject.put("prompt", entryWithColor.emotionEntry.note)
        noteJsonObject.put("name", storedUsername)
        noteJsonObject.put("emotion", entryWithColor.emotionEntry.emotion)

        lifecycleScope.launch (Dispatchers.IO){
            val entrySolution = entrySolutionDao.getSolution(entryWithColor.emotionEntry.id)
            if(entrySolution != null){
                withContext(Dispatchers.Main){
                    setProgressInvisible()
                    solutionText.text = entrySolution.solution
                }

            }else{
                lateinit var response: HttpResponse
                try{
                    response = client.post("https://proud-dashing-boxer.ngrok-free.app/prompt"){
                        contentType(ContentType.Application.Json)
                        setBody(noteJsonObject.toString())
                    }
                } catch (e:Exception){
                    withContext(Dispatchers.Main) {
                        val toast = Toast.makeText(
                            this@CompleteEntryActivity,
                            "Unable to connect to virtual counsellor. ${e.toString()}",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                        setProgressInvisible()
                        cancel()
                    }
                }

//                val body = response.body<LLMResponse>()
                val solution = response.body<LLMResponse>().response
                val solutionParts = solution.split("\n")
                val sb = StringBuilder()
                solutionParts.forEach {
                    if(it.isNotEmpty()){
                        sb.append(it)
                        sb.append("\n\n")
                    }
                }
                val formattedText = sb.toString()
                entrySolutionDao.insertSolution(EntrySolution(formattedText, entryWithColor.emotionEntry.id))
                withContext(Dispatchers.Main){
                    setProgressInvisible()
                    solutionText.text = formattedText
                }
            }
        }

//        lifecycleScope.launch (Dispatchers.IO){
//            val response = client.post("http://10.0.2.2:8000/prompt"){
//                contentType(ContentType.Application.Json)
//                setBody(noteJsonObject.toString())
//            }
//
//            withContext(Dispatchers.Main){
//                setProgressInvisible()
//                val body = response.body<LLMResponse>()
//                solutionText.text = body.response
//            }
//        }
    }

    private fun setProgressInvisible(){
        val progressLoader = findViewById<ProgressBar>(R.id.progress_loader)
        val progressLoaderLabel = findViewById<TextView>(R.id.progress_loader_label)
        progressLoader.visibility = View.INVISIBLE
        progressLoaderLabel.visibility = View.INVISIBLE
    }

    private fun setCompleteEntry(entryWithColor: EntryWithColor){
        val entryCardEmotionColor = findViewById<CardView>(R.id.entry_card_emotion_color)
        entryCardEmotionColor.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(entryWithColor.emotionColor.color)))
        val entryCardLabel = findViewById<TextView>(R.id.entry_card_label)
        entryCardLabel.text = entryWithColor.emotionEntry.label
        val entryCardTime = findViewById<TextView>(R.id.entry_card_time)
        entryCardTime.text = "${entryWithColor.emotionEntry.time}, ${entryWithColor.emotionEntry.date}"
        val entryCardNote = findViewById<TextView>(R.id.entry_card_note)
        entryCardNote.text = entryWithColor.emotionEntry.note
        val entryDeleteButton = findViewById<Button>(R.id.entry_delete_button)
        entryDeleteButton.setOnClickListener {
            deleteEntry(entryWithColor)
        }
    }

    private fun deleteEntry(entryWithColor: EntryWithColor){
        val emotionEntry = entryWithColor.emotionEntry
        val database = EmotionDatabase.getInstance(this@CompleteEntryActivity)
        val emotionEntryDao = database.emotionEntryDao()
        val entrySolutionDao = database.entrySolutionDao()
        lifecycleScope.launch (Dispatchers.IO){
            emotionEntryDao.deleteEmotionEntry(emotionEntry)
            val entrySolution = entrySolutionDao.getSolution(entryWithColor.emotionEntry.id)
            if(entrySolution!=null){
                entrySolutionDao.deleteSolution(entrySolution)
            }
            withContext(Dispatchers.Main){
                val intent = Intent(this@CompleteEntryActivity, WeeklyActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
    }
}