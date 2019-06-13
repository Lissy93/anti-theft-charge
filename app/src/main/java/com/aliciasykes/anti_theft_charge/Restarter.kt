package com.aliciasykes.anti_theft_charge

import android.content.Intent
import android.os.Build
import android.widget.Toast
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log


class Restarter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.i("Broadcast Listened", "Service tried to stop")
        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, PowerConnectionService::class.java))
        } else {
            context.startService(Intent(context, PowerConnectionService::class.java))
        }
    }
}