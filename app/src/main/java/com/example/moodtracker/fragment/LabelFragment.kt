package com.example.moodtracker.fragment

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moodtracker.R
import com.example.moodtracker.adapter.LabelButtonAdapter
import com.example.moodtracker.dao.LabelDao
import com.example.moodtracker.database.EmotionDatabase
import com.example.moodtracker.entity.Label
import com.example.moodtracker.utils.EntryPersistUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LabelFragment : Fragment(R.layout.fragment_label) {
    private lateinit var changeEntryBottom: ChangeEntryBottom

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val database = EmotionDatabase.getInstance(view.context)
        val labelDao = database.labelDao()
        val labelButtonRecyclerView = view.findViewById<RecyclerView>(R.id.label_recycler_view)
        val labelCard = view.findViewById<CardView>(R.id.label_card)
        val labelAddButton = view.findViewById<Button>(R.id.label_add_button)

        labelCard.setCardBackgroundColor(Color.parseColor(EntryPersistUtil.getEntry().color))

        lifecycleScope.launch (Dispatchers.IO){
            val labelList = labelDao.getLabels()
            withContext(Dispatchers.Main){
                val layoutManager = GridLayoutManager(view.context, 2)
                val labelButtonAdapter = LabelButtonAdapter(labelList, changeEntryBottom)
                labelButtonRecyclerView.adapter = labelButtonAdapter
                labelButtonRecyclerView.layoutManager = layoutManager
                labelAddButton.setOnClickListener {
                    addLabel(labelButtonAdapter, labelDao, labelList)
                }
            }
        }
    }

    private fun addLabel(labelButtonAdapter: LabelButtonAdapter, labelDao: LabelDao, labelList:List<Label>) {
        val addLabelDialog = Dialog(requireActivity(), R.style.DialogTheme)
        addLabelDialog.setContentView(R.layout.label_dialog)
        val labelText = addLabelDialog.findViewById<EditText>(R.id.label_text)
        labelText.requestFocus()
        val submitButton = addLabelDialog.findViewById<Button>(R.id.submit_label_button)
        submitButton.setOnClickListener {
            if(labelList.filter{it.labelName==labelText.text.toString()}.size == 1){
                val toast = Toast.makeText(requireActivity(), "Label already exists", Toast.LENGTH_SHORT)
                toast.show()
            }else{
                lifecycleScope.launch (Dispatchers.IO){
                    labelDao.addLabel(Label(labelText.text.toString()))
                    val newList = labelDao.getLabels()
                    withContext(Dispatchers.Main){
                        labelButtonAdapter.setList(newList)
                        labelButtonAdapter.notifyItemChanged(newList.size)
                        addLabelDialog.dismiss()
                    }
                }
            }
        }

        addLabelDialog.show()
    }

    fun setChangeEntryBottom(changeEntryBottom: ChangeEntryBottom){
        this.changeEntryBottom = changeEntryBottom
    }
}