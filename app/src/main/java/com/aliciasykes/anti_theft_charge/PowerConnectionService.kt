package com.aliciasykes.anti_theft_charge

import android.content.Intent
import android.content.BroadcastReceiver
import android.os.IBinder
import android.content.IntentFilter
import android.app.Service
import android.content.Context

class PowerConnectionService : Service() {

    /**
     * Initiate the intent, and register the receiver
     * This is only run on Nougat and above
     */
    override fun onCreate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            val connectionChangedIntent = IntentFilter()
            connectionChangedIntent.addAction(Intent.ACTION_POWER_CONNECTED)
            connectionChangedIntent.addAction(Intent.ACTION_POWER_DISCONNECTED)
            registerReceiver(connectionChangedReceiver, connectionChangedIntent)
        }
    }

    /**
     * Called when connection state changes
     * Checks if power was connected/ disconnected and calls appropriate method
     */
    private var connectionChangedReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // This block gets run whenever the power connection state is changed
            when {
                intent.action == Intent.ACTION_POWER_CONNECTED ->
                    powerWasConnected()
                intent.action == Intent.ACTION_POWER_DISCONNECTED ->
                    powerWasDisconnected()
            }
        }
    }

    /**
     * When the charger is connected, updates the central power status state
     */
    private fun powerWasConnected() {
        CurrentStatus.isConnected = true
        CurrentStatus.armDisarmFunctionality.powerConnected()
    }

    /**
     * When the charger is disconnected, updates the central power status state
     */
    private fun powerWasDisconnected() {
        CurrentStatus.isConnected = false
        CurrentStatus.armDisarmFunctionality.powerDisconnected()
    }

    override fun onStartCommand(resultIntent: Intent, resultCode: Int, startId: Int): Int {
        return startId
    }

    /**
     * Deregister the receiver
     * Initialise the broadcast intent, which will keep app running in background
     */
    override fun onDestroy() {
        super.onDestroy()
        try { // Receiver won't be registered on pre-Android 7 devices
            unregisterReceiver(connectionChangedReceiver)
        }
        catch (e: Throwable) {
            // Do fuck all
        }
//        val broadcastIntent = Intent()
//        broadcastIntent.action = "restartservice"
//        broadcastIntent.setClass(this, Restarter::class.java)
//        this.sendBroadcast(broadcastIntent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
