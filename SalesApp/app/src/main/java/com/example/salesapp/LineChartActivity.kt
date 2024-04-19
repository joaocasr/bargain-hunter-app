package com.example.salesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.example.salesapp.repository.SalesRepository
import com.google.firebase.firestore.FirebaseFirestore


class LineChartActivity : AppCompatActivity() {
    lateinit var anyChartView: AnyChartView
    lateinit var goback: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linechart)


        anyChartView = findViewById(R.id.linear_chart_view);
        anyChartView.setBackgroundColor("#2f3241");
        goback = findViewById(R.id.gobackbutton)

        val firebaseDatabase = FirebaseFirestore.getInstance()
        val productRepo = SalesRepository(firebaseDatabase)

        val cartesian = AnyChart.line()

        cartesian.animation(true)

        cartesian.padding(10.0, 20.0, 5.0, 20.0)

        cartesian.crosshair().enabled(true)
        cartesian.crosshair().yLabel(true).yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.title("Medium percentage of discounts each day.")
        cartesian.yAxis(0).title("Percentage of products discounts (%)")
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val seriesData: MutableList<DataEntry> = ArrayList()

        productRepo.getSalesByDay { listdays->
            if(listdays!=null){
                for(day in listdays){
                    seriesData.add(CustomDataEntry(day.data,day.productslist.getMediaDiscount()))
                }
            }
            val set = Set.instantiate()
            set.data(seriesData)
            val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")

            val series1 = cartesian.line(series1Mapping)
            series1.hovered().markers().enabled(true)
            series1.hovered().markers().type(MarkerType.CIRCLE).size(4.0)
            series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)
            series1.name("Discount Variation")

            cartesian.legend().enabled(true)
            cartesian.legend().fontSize(13.0)

            cartesian.background().enabled(true)
            cartesian.background().fill("#2f3241");
            anyChartView.setChart(cartesian)
        }

        goback.setOnClickListener {
            val intent = Intent(this,ChartsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0)
        }

    }

    private class CustomDataEntry internal constructor(
        x: String?,
        value: Double?
    ) :
        ValueDataEntry(x, value) {
        init {
        }
    }
}