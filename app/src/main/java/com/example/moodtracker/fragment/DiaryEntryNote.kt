package com.example.moodtracker.fragment

import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.moodtracker.R
import com.example.moodtracker.utils.EntryPersistUtil
import java.time.LocalTime

class DiaryEntryNote : Fragment(R.layout.fragment_diary_entry_note) {
    private lateinit var saveEntryInterface: SaveEntry
    private lateinit var changeEntryBottom: ChangeEntryBottom;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val note = view.findViewById<EditText>(R.id.emotion_note)
        val noteCard = view.findViewById<CardView>(R.id.emotion_note_card)
        val submitButton = view.findViewById<Button>(R.id.note_submit)

        noteCard.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(EntryPersistUtil.getEntry().color)))
        note.requestFocus()

        submitButton.setOnClickListener{
            val note = note.text.toString()
            if(note.isEmpty()){
                val toast = Toast.makeText(view.context, "Note is empty",Toast.LENGTH_SHORT)
                toast.show()
            }else if(note.length<15){
                val toast = Toast.makeText(view.context, "Insufficient Note",Toast.LENGTH_SHORT)
                toast.show()
            }
            else{
                EntryPersistUtil.getEntry().note = note
                val presentTime = LocalTime.now()
                val timePickerDialog = TimePickerDialog(view.context,R.style.TimePickerDialog,TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val pickedTime = LocalTime.of(hour, minute)
                    saveEntryInterface.saveEntry(pickedTime)
                }, presentTime.hour, presentTime.minute, true)
                timePickerDialog.show()
            }
        }
    }

    fun setSaveEntryInterface(saveEntryInterface: SaveEntry){
        this.saveEntryInterface = saveEntryInterface
    }

    fun setChangeEntryBottom(changeEntryBottom: ChangeEntryBottom){
        this.changeEntryBottom = changeEntryBottom
    }
}