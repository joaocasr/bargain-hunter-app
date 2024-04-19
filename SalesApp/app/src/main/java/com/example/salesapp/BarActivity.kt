package com.example.salesapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.anychart.core.cartesian.series.Column;
import com.example.salesapp.model.Product
import com.example.salesapp.repository.SalesRepository
import com.google.firebase.firestore.FirebaseFirestore
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.streams.toList

class BarActivity : AppCompatActivity() {
    lateinit var anyChartView: AnyChartView
    lateinit var goback: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar)

        goback = findViewById(R.id.gobackbutton)
        anyChartView = findViewById(R.id.bar_chart_view)
        anyChartView.setBackgroundColor("#2f3241");

        val firebaseDatabase = FirebaseFirestore.getInstance()
        val productRepo = SalesRepository(firebaseDatabase)


        productRepo.getSalesByDay {  it->
            val cartesian : Cartesian = AnyChart.column()
            val data: MutableList<DataEntry> = ArrayList()
            val prodsByStreet: Map<String,List<Product>> = it!!.map { it.productslist }.map { it.groupByStreet() }.flatMap { it.values }.flatten().stream()
                .collect(Collectors.groupingBy { it.rua })
            var sorted:Stream<Map.Entry<String,List<Product>>> = prodsByStreet.entries.stream().sorted(
                compareByDescending { it.value.size })
            var n = 1
            for((key,v) in sorted){
                println(key)
                println(v.size)
                data.add(ValueDataEntry(key,v.size))
                if(n==10) break;
                n+=1
            }
            val column: Column = cartesian.column(data)

            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .width(300)
                .height(100)
                .offsetX(0.0)
                .offsetY(1.0)
                .format("{%Value}{groupsSeparator: }")

            cartesian.background().fill("#2f3241");
            cartesian.animation(true)
            cartesian.title("Top 10 stores with more discounts")

            cartesian.yScale().minimum(0.0)

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title("Streets")
            cartesian.yAxis(0).title("Number of products with discounts")

            anyChartView.setChart(cartesian)

        }

        goback.setOnClickListener {
            val intent = Intent(this,ChartsActivity::class.java)
            startActivity(intent)
        }
    }
}