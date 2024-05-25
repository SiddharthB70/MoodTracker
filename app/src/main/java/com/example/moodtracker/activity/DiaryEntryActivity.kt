package com.example.moodtracker.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import com.example.moodtracker.R
import com.example.moodtracker.database.EmotionDatabase
import com.example.moodtracker.entity.EmotionEntry
import com.example.moodtracker.fragment.ChangeEntryBottom
import com.example.moodtracker.fragment.DiaryEntryEmotion
import com.example.moodtracker.fragment.DiaryEntryMessage
import com.example.moodtracker.fragment.DiaryEntryNote
import com.example.moodtracker.fragment.LabelFragment
import com.example.moodtracker.fragment.SaveEntry
import com.example.moodtracker.utils.EntryPersistUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalTime

class DiaryEntryActivity : AppCompatActivity(), ChangeEntryBottom, SaveEntry {
    private val diaryEntryMessage = DiaryEntryMessage()
    private val diaryEntryEmotion = DiaryEntryEmotion()
    private val diaryEntryNote = DiaryEntryNote()
    private val labelFragment = LabelFragment()

    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_entry)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.diaryEntryTopContainer, diaryEntryMessage)
            replace(R.id.diaryEntryBottomContainer, diaryEntryEmotion)
            commit()
        }
        diaryEntryEmotion.setChangeEntryBottom(this)
        diaryEntryNote.setChangeEntryBottom(this)
        diaryEntryNote.setSaveEntryInterface(this)
        labelFragment.setChangeEntryBottom(this)

        val sharedPref = getSharedPreferences("usernamePref",Context.MODE_PRIVATE)
        val storedUsername: String? = sharedPref.getString("username","")
        diaryEntryMessage.lifecycleScope.launch {
            withStarted {
                typeMessage("Hello $storedUsername. How do you do?")
            }
        }
    }

    private fun typeMessage(text:String){
        diaryEntryMessage.typeWrite(this, text)
    }

    override fun setBottomContainerLabelFragment(emotion:String, color:String) {
        diaryEntryMessage.typeWrite(this, " What are you feeling ${emotion.lowercase()} about?")
        EntryPersistUtil.getEntry().emotion = emotion
        EntryPersistUtil.getEntry().color = color
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.diaryEntryBottomContainer, labelFragment)
            commit()
        }
    }

    override fun setBottomContainerNoteFragment(label: String) {
        val entry = EntryPersistUtil.getEntry()
        entry.label = label
        diaryEntryMessage.typeWrite(this, " Why does ${label.lowercase()} make you ${entry.emotion.lowercase()}?")
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.diaryEntryBottomContainer, diaryEntryNote)
            commit()
        }
    }

    override fun saveEntry(time:LocalTime) {
        val entry = EntryPersistUtil.getEntry()
        val dbEntry = EmotionEntry(entry.emotion, entry.label, entry.note, entry.date.toString(), time.toString())
        val database = EmotionDatabase.getInstance(this@DiaryEntryActivity)
        val emotionEntryDao = database.emotionEntryDao()
        lifecycleScope.launch (Dispatchers.IO){
            emotionEntryDao.insertEmotionEntry(dbEntry)
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(this@DiaryEntryActivity,"Saved Entry!", Toast.LENGTH_SHORT)
                toast.show()
                val intent = Intent(this@DiaryEntryActivity, WeeklyActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }
    }
}