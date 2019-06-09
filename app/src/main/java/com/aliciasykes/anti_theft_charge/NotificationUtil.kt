package com.aliciasykes.anti_theft_charge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat

class NotificationUtil(_mainActivity: MainActivity) {

    private var mainActivity: MainActivity = _mainActivity

    /**
     * Receives a title and description, then initialises the chanel and displays the notification
     */
    fun showNotification(title: String, description: String, channelId: String){
        createNotificationChannel(channelId)
        val builder = NotificationCompat.Builder(mainActivity, channelId)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(title)
            .setContentText(description)
            .setStyle(NotificationCompat.BigTextStyle())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(mainActivity)) {
            notify(1, builder.build())
        }
    }

    /**
     * Create the notification chanel, and set the importance
     */
    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = mainActivity.getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = mainActivity.getString(R.string.what_is_notification)
            val notificationManager =
                mainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
