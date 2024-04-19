package com.example.salesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChartsActivity : AppCompatActivity() {
    lateinit var linecard:CardView
    lateinit var barcard:CardView
    lateinit var navigation : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charts)

        linecard = findViewById(R.id.linechart)
        barcard = findViewById(R.id.barchart)
        navigation = findViewById(R.id.bottom_navigation3)

        linecard.setOnClickListener {
            val intentline1 = Intent(this,LineChartActivity::class.java)
            startActivity(intentline1)
            overridePendingTransition(0,0)
        }
        barcard.setOnClickListener {
            val intentline2 = Intent(this,BarActivity::class.java)
            startActivity(intentline2)
            overridePendingTransition(0,0)
        }

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
                R.id.nav_visualization -> true
                else -> {true}
            }
        }



    }
}