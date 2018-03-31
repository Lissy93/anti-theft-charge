package com.aliciasykes.anti_theft_charge

import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat



class NotificationUtil(_mainActivity: MainActivity) {

    private var mainActivity: MainActivity = _mainActivity

    fun showNotification(){
        val mBuilder = NotificationCompat.Builder(mainActivity, "atc")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Protected")
                .setContentText("Lorem Ipsum Dolor")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(mainActivity)
        notificationManager.notify(1, mBuilder.build())
    }
}