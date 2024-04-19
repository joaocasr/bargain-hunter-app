package com.example.salesapp.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.salesapp.NearestActivity
import com.example.salesapp.R

class MyNotificationService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        generate_notification(intent)
        return START_STICKY
    }

    fun getRemoteView(title: String, body: String): RemoteViews {
        val remoteView = RemoteViews("com.example.salesapp",R.layout.notification)
        remoteView.setTextViewText(R.id.notificationtitle,title)
        remoteView.setTextViewText(R.id.notificationbody,body)
        remoteView.setImageViewResource(R.id.notificationicon,R.drawable.ic_launcher_foreground)
        return remoteView
    }

    fun generate_notification(myintent: Intent?) {
        val intent = Intent(this, NearestActivity::class.java)
        val randomproduct: String? = myintent?.getStringExtra("randomproduct")

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_UPDATE_CURRENT)

        var builder : NotificationCompat.Builder = NotificationCompat.Builder(this,"notification_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(NotificationCompat.BigTextStyle().bigText("New product discounts were detected near where you are. Check the new $randomproduct."))
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView("New Discounts",
            "New product discounts were detected near where you are. Check the new $randomproduct."
        ))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel("notification_channel","com.example.salesapp",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}