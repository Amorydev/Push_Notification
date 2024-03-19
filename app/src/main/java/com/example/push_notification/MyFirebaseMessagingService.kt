package com.example.push_notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService :FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification !=null)
        {
            showNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
    @SuppressLint("ObsoleteSdkInt")
    private fun showNotification(title:String, message:String){
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val channel_id = "notifiaction_id"

        //tạo pendingIntent
        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder:NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,channel_id)
            .setSmallIcon(R.drawable.img)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteViews(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel =NotificationChannel(channel_id,"notification",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteViews(title: String, message: String): RemoteViews? {
        val remoteViews = RemoteViews("com.example.push_notification",R.layout.notification)
        remoteViews.setTextViewText(R.id.title,title)
        remoteViews.setTextViewText(R.id.message,message)
        remoteViews.setImageViewResource(R.id.logo,R.drawable.img)
        return remoteViews
    }
}