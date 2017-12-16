package com.aliciasykes.anti_theft_charge

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.content.BroadcastReceiver

class ChargingUtil {

    /**
     * Determines wheather the device is currently plugged-in
     * @param context
     * @return boolean
     */
    fun isConnected(context: Context): Boolean {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        CurrentStatus.isConnected = plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
        return CurrentStatus.isConnected
    }

    class PlugInReceiver : BroadcastReceiver() {

        /**
         * Called when BroadcastReceiver emits a power connected or disconnected change
         * Updates static powerConnected reference and calls to update the UI
         */
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_POWER_CONNECTED) {
                CurrentStatus.isConnected = true
                CurrentStatus.armDisarmFunctionality.powerConnected()
            }
            else if (intent.action == Intent.ACTION_POWER_DISCONNECTED) {
                CurrentStatus.isConnected = false
                CurrentStatus.armDisarmFunctionality.powerDisconnected()
            }
        }
    }


}


