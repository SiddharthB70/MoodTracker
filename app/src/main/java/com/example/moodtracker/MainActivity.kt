package com.example.moodtracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.moodtracker.activity.WeeklyActivity

class
MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences("usernamePref",Context.MODE_PRIVATE)
        val storedUsername: String? = sharedPref.getString("username", null)
        if(storedUsername!=null){
            startActivity(Intent(this, WeeklyActivity::class.java))
            finish()
        }else{
            setContentView(R.layout.activity_main)
            val usernameText = findViewById<EditText>(R.id.username)
            val submitButton = findViewById<Button>(R.id.submit_username_button)
            submitButton.setOnClickListener {
                val username = usernameText.text.toString()
                if(username.isEmpty()){
                    val toast = Toast.makeText(this@MainActivity, "Empty username not allowed!", Toast.LENGTH_SHORT)
                    toast.show()
                }else{
                    with(sharedPref.edit()){
                        putString("username", username)
                        apply()
                    }
                    startActivity(Intent(this, WeeklyActivity::class.java))
                    finish()
                }
            }
        }
    }
}