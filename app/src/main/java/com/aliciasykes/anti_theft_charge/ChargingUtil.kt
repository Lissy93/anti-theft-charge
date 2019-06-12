package com.aliciasykes.anti_theft_charge

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.content.BroadcastReceiver

class ChargingUtil (context: Context? = null){

    init {
        if(context != null) checkIfDeviceConnected(context)
    }

    /**
     * Determines weather the device is currently plugged-in
     * @param context
     * @return boolean
     */
    private fun checkIfDeviceConnected(context: Context): Boolean {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        CurrentStatus.isConnected = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
        return CurrentStatus.isConnected
    }

    class PlugInReceiver : BroadcastReceiver() {

        /**
         * Called when BroadcastReceiver emits a power connected or disconnected change
         * Updates static powerConnected reference and calls to update the UI
         * This is a fallback for pre Android 7 systems
         */
        override fun onReceive(context: Context, intent: Intent) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                if (intent.action == Intent.ACTION_POWER_CONNECTED) {
                    CurrentStatus.isConnected = true
                    CurrentStatus.armDisarmFunctionality.powerConnected()
                } else if (intent.action == Intent.ACTION_POWER_DISCONNECTED) {
                    CurrentStatus.isConnected = false
                    CurrentStatus.armDisarmFunctionality.powerDisconnected()
                }
            }
        }
    }
}


