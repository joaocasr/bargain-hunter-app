package com.example.salesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {
    lateinit var signout : CardView
    lateinit var goback : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        signout = findViewById(R.id.signoutcard)
        goback = findViewById(R.id.gobackbutton)

        goback.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        signout.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()
            val intent = Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }

    }
}