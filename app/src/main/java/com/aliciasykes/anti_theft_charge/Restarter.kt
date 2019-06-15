package com.aliciasykes.anti_theft_charge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, PowerConnectionService::class.java))
        } else {
            context.startService(Intent(context, PowerConnectionService::class.java))
        }
    }
}