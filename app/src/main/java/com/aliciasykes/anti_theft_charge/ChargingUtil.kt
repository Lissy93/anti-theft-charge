package com.aliciasykes.anti_theft_charge

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.content.BroadcastReceiver

class ChargingUtil(_armDisarmFunctionality: ArmDisarmFunctionality) {

    private var armDisarmFunctionality: ArmDisarmFunctionality = _armDisarmFunctionality


        private var connected: Boolean = false

    /**
     * Determines wheather the device is currently plugged-in
     * @param context
     * @return boolean
     */
    fun isConnected(context: Context): Boolean {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
    }


    class PlugInReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_POWER_CONNECTED) {
                System.out.println("Conected.")
                // Do something when power connected
            } else if (action == Intent.ACTION_POWER_DISCONNECTED) {
                System.out.println("Disconnected.")
                // Do something when power disconnected
            }
        }
    }
}


