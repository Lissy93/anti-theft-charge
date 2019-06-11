package com.aliciasykes.anti_theft_charge

import android.content.Intent
import android.content.BroadcastReceiver
import android.os.IBinder
import android.content.IntentFilter
import android.app.Service
import android.content.Context
import android.widget.Toast

class PowerConnectionService : Service() {

    private var connectionChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(applicationContext, "This got hit", Toast.LENGTH_LONG).show()
            when {
                intent.action == Intent.ACTION_POWER_CONNECTED -> powerWasConnected()
                intent.action == Intent.ACTION_POWER_DISCONNECTED -> powerWasDisconnected()
            }
        }
    }

    override fun onCreate() {
        val connectionChangedIntent = IntentFilter()
        connectionChangedIntent.addAction(Intent.ACTION_POWER_CONNECTED)
        connectionChangedIntent.addAction(Intent.ACTION_POWER_DISCONNECTED)
        registerReceiver(connectionChangedReceiver, connectionChangedIntent)
    }

    override fun onStartCommand(resultIntent: Intent, resultCode: Int, startId: Int): Int {
        return startId
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectionChangedReceiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun powerWasConnected() {
        CurrentStatus.isConnected = true
        CurrentStatus.armDisarmFunctionality.powerConnected()
    }
    private fun powerWasDisconnected() {
        CurrentStatus.isConnected = false
        CurrentStatus.armDisarmFunctionality.powerDisconnected()
    }
}
