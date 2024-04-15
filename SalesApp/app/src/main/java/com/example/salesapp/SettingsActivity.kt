package com.example.salesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {
    lateinit var navigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        navigation = findViewById(R.id.bottom_navigation3)

        navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    startActivity(Intent(applicationContext,MainActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_nearest -> {
                    startActivity(Intent(applicationContext,NearestActivity::class.java))
                    overridePendingTransition(0,0)
                    true
                }
                R.id.nav_settings -> true
                else -> {true}            }
        }
    }
}